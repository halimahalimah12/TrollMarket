package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.ExcelUtility;
import com.TrollMarket.TrollMarket.dtos.order.OrderRequestDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderRowDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderSearchDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderToDatabaseDto;
import com.TrollMarket.TrollMarket.dtos.utility.SelectBuyerDto;
import com.TrollMarket.TrollMarket.dtos.utility.SelectListSellerDto;
import com.TrollMarket.TrollMarket.models.*;
import com.TrollMarket.TrollMarket.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private  final ShipmentRepository shipmentRepository;
    private final  ProductRepository productRepository;


    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, BuyerRepository buyerRepository, SellerRepository sellerRepository, ShipmentRepository shipmentRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
        this.shipmentRepository = shipmentRepository;
        this.productRepository = productRepository;
    }

    public List<OrderRowDto> getAll(OrderSearchDto dto){
        Integer seller = dto.getSeller() == null || dto.getSeller() == 0 ? null : dto.getSeller();
        Integer buyer = dto.getBuyer() == null || dto.getBuyer() == 0 ? null : dto.getBuyer();


        List<Order> orderList = orderRepository.findAllBySearch(seller,buyer);
        List<OrderRowDto> orderRowDtos =  orderList.stream().map(
                order -> OrderRowDto.builder()
                        .date(order.getOrderDate())
                        .seller(order.getProduct().getSeller().getName())
                        .buyer(order.getBuyer().getName())
                        .product(order.getProduct().getName())
                        .quantity(order.getQuantity())
                        .shipment(order.getShipment().getName())
                        .totalPrice(totalPrice(order.getUnitPrice(),order.getQuantity(),order.getShipment().getPrice()))
                        .build())
                .toList();
        return orderRowDtos;

    }

    public List<OrderRowDto> getAllOrderExcel(){
        List<Order> orderList = orderRepository.findAll();
        List<OrderRowDto> orderRowDtos =  orderList.stream().map(
                        order -> OrderRowDto.builder()
                                .date(order.getOrderDate())
                                .seller(order.getProduct().getSeller().getName())
                                .buyer(order.getBuyer().getName())
                                .product(order.getProduct().getName())
                                .quantity(order.getQuantity())
                                .shipment(order.getShipment().getName())
                                .shipmentPrice(order.getFreight())
                                .totalPrice(totalPrice(order.getUnitPrice(),order.getQuantity(),order.getShipment().getPrice()))
                                .build())
                .toList();
        return orderRowDtos;

    }

    public Double totalPrice(Double unitPrice, Integer quantity, Double shipmentPrice) {
        return (unitPrice * quantity) + shipmentPrice;
    }

    public  List<SelectListSellerDto> getSellerDropdown() {
        return sellerRepository.findAll().stream()
                .map(seller -> SelectListSellerDto.builder()
                        .value(seller.getId())
                        .text(seller.getName())
                        .build())
                .toList();
    }
    public List<SelectBuyerDto> getBuyerDropdown() {
        return buyerRepository.findAll().stream()
                .map(buyer -> SelectBuyerDto.builder()
                        .value(buyer.getId())
                        .text(buyer.getName())
                        .build())
                .toList();
    }


    public String save(OrderRequestDto dto) {

        List<Cart> cartList = cartRepository.findAllByBuyer(dto.getBuyerId());

        Buyer buyer = buyerRepository.findById(dto.getBuyerId()).orElseThrow(() -> new IllegalArgumentException("Id not found"));

        Double totalPrice = 0.0;
        for (Cart cart : cartList) {
            totalPrice += (cart.getQuantity() * cart.getProduct().getPrice()) + cart.getShipment().getPrice();
        }

        if (totalPrice < buyer.getBalance()) {
            for (Cart cart : cartList) {
                Double priceCart = (cart.getQuantity() * cart.getProduct().getPrice()) + cart.getShipment().getPrice();
                Order order = Order.builder()
                        .product(cart.getProduct())
                        .shipment(cart.getShipment())
                        .buyer(cart.getBuyer())
                        .quantity(cart.getQuantity())
                        .unitPrice(cart.getProduct().getPrice())
                        .freight(cart.getShipment().getPrice())
                        .orderDate(LocalDate.now())
                        .build();
                orderRepository.save(order);
                cartRepository.deleteById(cart.getId());

                buyer.setBalance(buyer.getBalance() - priceCart);
                buyerRepository.save(buyer);

                Seller seller = sellerRepository.findById(cart.getProduct().getSeller().getId())
                                .orElseThrow(()-> new IllegalArgumentException("Id nor found"));
                seller.setBalance(seller.getBalance()+priceCart);
                sellerRepository.save(seller);
            }

            return "Successfully new Order";
        } else {
            return "Gagal order karena saldo tidak cukup!";
        }

    }

    public  void  saveDataFromExcel(MultipartFile file){
        try {
            List<OrderToDatabaseDto> orderList = ExcelUtility.excelToList(file.getInputStream());
            orderRepository.deleteAll();

            for (var order : orderList){
                Buyer buyer = buyerRepository.buyerFindByName(order.getBuyer());
                Product product = productRepository.findProductByName(order.getProduct());
                Shipment shipment = shipmentRepository.shipmentFindByName(order.getShipment());

                Order order1 = Order.builder()
                        .orderDate(order.getDate())
                        .buyer(buyer)
                        .product(product)
                        .shipment(shipment)
                        .quantity(order.getQuantity())
                        .unitPrice(order.getUnitPrice())
                        .freight(order.getFreight())
                        .build();
                orderRepository.save(order1);
            }
        }catch (IOException ex){
            throw  new RuntimeException("Excel data is failde to store:" + ex.getMessage());
        }
    }
}
