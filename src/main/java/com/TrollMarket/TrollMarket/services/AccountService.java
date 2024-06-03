package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.auth.SelectListRoleDto;
import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.repositories.AccountRepository;
import com.TrollMarket.TrollMarket.repositories.BuyerRepository;
import com.TrollMarket.TrollMarket.repositories.SellerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;

    public AccountService(AccountRepository accountRepository, SellerRepository sellerRepository, BuyerRepository buyerRepository) {
        this.accountRepository = accountRepository;
        this.sellerRepository = sellerRepository;
        this.buyerRepository = buyerRepository;
    }

    public List<SelectListRoleDto> getRoleDropdown(String username) {

        List<SelectListRoleDto> roleList = new ArrayList<>();

        Buyer buyer = buyerRepository.buyerFindByAccound(username);
        if (buyer != null) {
            roleList.add(SelectListRoleDto.builder()
                    .id(buyer.getId())
                    .roleName("Buyer")
                    .build());
        }

        Seller seller = sellerRepository.sellerFindByAccound(username);
        if (seller != null) {
            roleList.add(SelectListRoleDto.builder()
                    .id(seller.getId())
                    .roleName("Seller")
                    .build());
        }

        if (seller == null && buyer == null) {
            roleList.add(SelectListRoleDto.builder()
                    .roleName("Admin")
                    .build());
        }

        return roleList;
    }

    public Buyer getBuyerActive(String username) {
        return buyerRepository.buyerFindByAccound(username);
    }

    public Seller getSellerActive(String username) {
        return sellerRepository.sellerFindByAccound(username);
    }


}
