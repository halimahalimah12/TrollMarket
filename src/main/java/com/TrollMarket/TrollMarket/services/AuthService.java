package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterSellerBuyerDto;
import com.TrollMarket.TrollMarket.models.Account;
import com.TrollMarket.TrollMarket.models.AccountDetail;
import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.repositories.AccountRepository;
import com.TrollMarket.TrollMarket.repositories.BuyerRepository;
import com.TrollMarket.TrollMarket.repositories.SellerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final AccountRepository accountRespository;
    private final AccountService accountService;
    public final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;

    public AuthService(AccountRepository accountRespository, AccountService accountService, PasswordEncoder passwordEncoder, SellerRepository sellerRepository, BuyerRepository buyerRepository) {
        this.accountRespository = accountRespository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepository = sellerRepository;
        this.buyerRepository = buyerRepository;
    }

    public void register(AuthRegisterDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password did not match");
        }

        var hashedPassword = passwordEncoder.encode(dto.getPassword());

        accountRespository.save(Account.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .build());
    }

    public void registerSeller(AuthRegisterSellerBuyerDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password did not match");
        }

        var hashedPassword = passwordEncoder.encode(dto.getPassword());

        var account =accountRespository.save(Account.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .build());

        Seller seller = Seller.builder()
                        .id(dto.id)
                        .account(account)
                        .name(dto.name)
                        .address(dto.address)
                        .balance(0.0)
                .build();
        sellerRepository.save(seller);

    }

    public void registerBuyer(AuthRegisterSellerBuyerDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password did not match");
        }

        var hashedPassword = passwordEncoder.encode(dto.getPassword());

        var account =accountRespository.save(Account.builder()
                .username(dto.getUsername())
                .password(hashedPassword)
                .build());

        Buyer buyer = Buyer.builder()
                .id(dto.id)
                .account(account)
                .name(dto.name)
                .address(dto.address)
                .balance(0.0)
                .build();
        buyerRepository.save(buyer);

    }


    public boolean isUSernameExist(String username) {
        return accountRespository.existsById(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var account = accountRespository.findById(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return AccountDetail.builder()
                .account(account)
                .accountService(accountService)
                .build();

    }
}
