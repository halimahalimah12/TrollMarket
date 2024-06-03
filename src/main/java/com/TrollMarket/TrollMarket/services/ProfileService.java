package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.profile.OrderProfileRowItemDto;
import com.TrollMarket.TrollMarket.repositories.BuyerRepository;
import com.TrollMarket.TrollMarket.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import com.TrollMarket.TrollMarket.models.Order;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;

    public ProfileService(BuyerRepository buyerRepository, OrderRepository orderRepository) {
        this.buyerRepository = buyerRepository;
        this.orderRepository = orderRepository;
    }
    public List<OrderProfileRowItemDto>  getAllOrderBuyer(Integer id){
        List<Order>  orderList= orderRepository.orderListByBuyer(id);

        return  orderList.stream()
                .map(this::convertOrderRowDto)
                .collect(Collectors.toList());
    }
    private OrderProfileRowItemDto convertOrderRowDto(Order order) {
        return OrderProfileRowItemDto.builder()
                .date(order.getOrderDate())
                .nameProduct(order.getProduct().getName())
                .quantity(order.getQuantity())
                .nameShipment(order.getShipment().getName())
                .totalPrice(totalPrice(order.getUnitPrice(),order.getQuantity(),order.getShipment().getPrice()))
                .build();
    }

    public Double totalPrice(Double unitPrice, Integer quantity, Double shipmentPrice) {
        return (unitPrice * quantity) + shipmentPrice;
    }

    public List<OrderProfileRowItemDto>  getAllOrderSeller(Integer id){
        List<Order>  orderList= orderRepository.orderListBySeller(id);

        return  orderList.stream()
                .map(this::convertOrderRowDto)
                .collect(Collectors.toList());
    }



}
