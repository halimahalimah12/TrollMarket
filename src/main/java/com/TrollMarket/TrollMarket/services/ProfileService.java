package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.profile.OrderProfileRowItemDto;
import com.TrollMarket.TrollMarket.dtos.profile.ProfileUpsertRequestDto;
import com.TrollMarket.TrollMarket.models.Account;
import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.repositories.AccountRepository;
import com.TrollMarket.TrollMarket.repositories.BuyerRepository;
import com.TrollMarket.TrollMarket.repositories.OrderRepository;
import com.TrollMarket.TrollMarket.repositories.SellerRepository;
import org.springframework.stereotype.Service;
import com.TrollMarket.TrollMarket.models.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import  java.nio.file.Files;
import java.nio.file.Path;
import  java.nio.file.Paths;
import java.io.IOException;

@Service
public class ProfileService {
    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;
    private final SellerRepository sellerRepository;
    private  final AccountRepository accountRepository;


    public ProfileService(BuyerRepository buyerRepository, OrderRepository orderRepository, SellerRepository sellerRepository, AccountRepository accountRepository) {
        this.buyerRepository = buyerRepository;
        this.orderRepository = orderRepository;
        this.sellerRepository = sellerRepository;
        this.accountRepository = accountRepository;
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

    public void updateProfil(ProfileUpsertRequestDto dto){
        var account = findAccount(dto.getAccountId());

        if(dto.getRole().equalsIgnoreCase("Seller")){
            var seller = findSeller(dto.getId());

            //image
            if(!dto.getPhoto().isEmpty()){
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + seller.getPhoto());

                try{
                    Files.delete(oldImagePath);
                }catch (Exception ex){
                    System.out.println("Exception:"+ex.getMessage());
                }

                //save new image file
                MultipartFile image = dto.getPhoto();
                Date createAt = new Date();
                String storageFileName = createAt.getTime()+"_"+ image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                var userSeller = Seller.builder()
                        .id(dto.getId())
                        .account(account)
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .balance(dto.getBalance())
                        .photo(storageFileName)
                        .build();
                sellerRepository.save(userSeller);


            }

        }

        if(dto.getRole().equalsIgnoreCase("Buyer") ){
            var buyer = findBuyer(dto.getId());

            //image
            if(!dto.getPhoto().isEmpty()){
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + buyer.getPhoto());

                try{
                    Files.delete(oldImagePath);
                }catch (Exception ex){
                    System.out.println("Exception:"+ex.getMessage());
                }

                //save new image file
                MultipartFile image = dto.getPhoto();
                Date createAt = new Date();
                String storageFileName = createAt.getTime()+"_"+ image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream,Paths.get(uploadDir+storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                var userBuyer = Buyer.builder()
                        .id(dto.getId())
                        .account(account)
                        .name(dto.getName())
                        .address(dto.getAddress())
                        .balance(dto.getBalance())
                        .photo(storageFileName)
                        .build();
                buyerRepository.save(userBuyer);


            }
        }
    }

    private Buyer findBuyer(Integer buyerId){
        return buyerRepository.findById(buyerId)
                .orElseThrow(() -> new  IllegalArgumentException("Seller not found"));
    }

    private Account findAccount(String username){
        return  accountRepository.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    private Seller findSeller(Integer sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new  IllegalArgumentException("Seller not found"));
    }



}
