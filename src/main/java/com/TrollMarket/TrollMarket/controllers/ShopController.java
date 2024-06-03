package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.shop.ShopSearchDto;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.services.ProductService;
import com.TrollMarket.TrollMarket.services.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("shop")
public class ShopController {

    private ShopService shopService;
    private InfoUserLogin infoUserLogin;
    private ProductService productService;

    public ShopController(ShopService shopService, InfoUserLogin infoUserLogin, ProductService productService) {
        this.shopService = shopService;
        this.infoUserLogin = infoUserLogin;
        this.productService = productService;
    }

    @GetMapping("")
    public ModelAndView index(ShopSearchDto dto) {
        ModelAndView mv = new ModelAndView("shop/index");
        Integer buyerId = infoUserLogin.buyerDetailActive().getId();
        mv.addObject("categoryDropdown",productService.getCategoryDropdown());
        mv.addObject("dto",dto);
        mv.addObject("products", shopService.getAllProdcut(dto) );
        mv.addObject("buyerId",buyerId);
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }


}
