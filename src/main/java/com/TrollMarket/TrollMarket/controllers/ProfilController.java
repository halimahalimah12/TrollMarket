package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.services.ProfileService;
import com.TrollMarket.TrollMarket.services.SellerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfilController {
    private final ProfileService profileService;
    private final SellerService sellerService;
    private final InfoUserLogin infoUserLogin;

    public ProfilController(ProfileService profileService, SellerService sellerService, InfoUserLogin infoUserLogin) {
        this.profileService = profileService;
        this.sellerService = sellerService;
        this.infoUserLogin = infoUserLogin;
    }

    @GetMapping("profile")
    public ModelAndView admin(){
        var mv = new ModelAndView("profile");
        String authoriActive=  infoUserLogin.authorizationActive();
        if (authoriActive == "Buyer" ){
            Integer buyerId= infoUserLogin.buyerDetailActive().getId();
            mv.addObject("orders",profileService.getAllOrderBuyer(buyerId));
            mv.addObject("buyerId",buyerId);
        }else if (authoriActive == "Seller"){
            Integer sellerID= infoUserLogin.sellerDetailActive().getId();
            mv.addObject("orders",profileService.getAllOrderSeller(sellerID));
        }else {}
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }
}
