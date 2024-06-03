package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.services.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("buyers")
public class BuyerController {
    private final ProfileService buyerService;
    private final InfoUserLogin infoUserLogin;

    public BuyerController(ProfileService buyerService, InfoUserLogin infoUserLogin) {
        this.buyerService = buyerService;
        this.infoUserLogin = infoUserLogin;
    }

//    @GetMapping("")
//    public ModelAndView index() {
//        ModelAndView modelAndView = new ModelAndView("");
//       modelAndView.addObject("orders",buyerService.getAllOrder());
//        return modelAndView;
//    }

}
