package com.TrollMarket.TrollMarket;

import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.services.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class InfoUserLogin {

    private static AccountService accountService;

    public InfoUserLogin(AccountService accountService) {
        this.accountService = accountService;
    }

    public  String authorizationActive(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        var authorization = authentication.getAuthorities();
        boolean isBuyer = false;
        boolean isSeller = false;
        for (GrantedAuthority authority : authorization){
            if (authority.getAuthority().equalsIgnoreCase("Buyer")) {
                isBuyer = true;
                break;
            } else if (authority.getAuthority().equalsIgnoreCase("Seller")) {
                isSeller = true;
                break;
            }
        }

        if (isBuyer) {
            return "Buyer";
        } else if (isSeller) {
          return "Seller";
        } else {
           return "Admin";
        }
    }

    public  void userDetailLogin(ModelAndView mv) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        var authorization = authentication.getAuthorities();
        boolean isBuyer = false;
        boolean isSeller = false;
        for (GrantedAuthority authority : authorization){
            if (authority.getAuthority().equalsIgnoreCase("Buyer")) {
                isBuyer = true;
                break;
            } else if (authority.getAuthority().equalsIgnoreCase("Seller")) {
                isSeller = true;
                break;
            }
        }

        if (isBuyer) {
            Buyer buyer = accountService.getBuyerActive(authentication.getName());
            mv.addObject("buyer", buyer);
            mv.addObject("role","Buyer");
        } else if (isSeller) {
            Seller seller = accountService.getSellerActive(authentication.getName());
            mv.addObject("seller", seller);
            mv.addObject("role","Seller");
        } else {
            mv.addObject("admin", authentication.getName());
            mv.addObject("role","Admin");

        }

    }
    public Seller sellerDetailActive() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Seller seller = accountService.getSellerActive(authentication.getName());
        return seller;
    }

    public Buyer buyerDetailActive() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Buyer buyer = accountService.getBuyerActive(authentication.getName());
        return buyer;
    }
}
