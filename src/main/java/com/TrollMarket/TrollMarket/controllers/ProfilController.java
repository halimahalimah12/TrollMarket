package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.profile.ProfileUpsertRequestDto;
import com.TrollMarket.TrollMarket.services.ProfileService;
import com.TrollMarket.TrollMarket.services.SellerService;
import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("profile")
public class ProfilController {
    private final ProfileService profileService;
    private final SellerService sellerService;
    private final InfoUserLogin infoUserLogin;

    public ProfilController(ProfileService profileService, SellerService sellerService, InfoUserLogin infoUserLogin) {
        this.profileService = profileService;
        this.sellerService = sellerService;
        this.infoUserLogin = infoUserLogin;
    }

    @GetMapping("")
    public ModelAndView admin(){
        var mv = new ModelAndView("profile");
        String authoriActive=  infoUserLogin.authorizationActive();
        if (authoriActive == "Buyer" ){
            Integer buyerId= infoUserLogin.buyerDetailActive().getId();
            mv.addObject("orders",profileService.getAllOrderBuyer(buyerId));
            mv.addObject("buyerId",buyerId);
        }
        if (authoriActive == "Seller"){
            Integer sellerID= infoUserLogin.sellerDetailActive().getId();
            mv.addObject("orders",profileService.getAllOrderSeller(sellerID));

        }
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @GetMapping("update")
    public  ModelAndView update (){
        ModelAndView mv = new ModelAndView("ProfileUpdate");
        String authoriActive=  infoUserLogin.authorizationActive();
        if (authoriActive == "Seller"){
            var seller= infoUserLogin.sellerDetailActive();
            mv.addObject("role","Seller");
            mv.addObject("dto",seller );
        }
        if(authoriActive == "Buyer"){
            var buyer = infoUserLogin.buyerDetailActive();
            mv.addObject("role","Buyer");
            mv.addObject("dto",buyer);
        }

        return mv;

    }

    @PostMapping("/upsert")
    private String post(@ModelAttribute("dto") ProfileUpsertRequestDto dto){

        profileService.updateProfil(dto);
        return "redirect:/profile";
    }
}
