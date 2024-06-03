package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.order.OrderRequestDto;
import com.TrollMarket.TrollMarket.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("carts")
public class CartController {
    private final InfoUserLogin infoUserLogin;
    private  final OrderService orderService;

    public CartController(InfoUserLogin infoUserLogin, OrderService orderService) {
        this.infoUserLogin = infoUserLogin;
        this.orderService = orderService;
    }

    @GetMapping("")
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView("cart/index");
        Integer buyerId = infoUserLogin.buyerDetailActive().getId();
        modelAndView.addObject("buyerId",buyerId);
        if (model.containsAttribute("info")) {
            modelAndView.addObject("info", model.getAttribute("info"));
        }
        infoUserLogin.userDetailLogin(modelAndView);
        return modelAndView;
    }

    @PostMapping("upsert")
    public  String purchoseAll(OrderRequestDto dto,RedirectAttributes redirectAttributes){
        String pesan = orderService.save(dto);
        redirectAttributes.addFlashAttribute("info",pesan);
        return "redirect:/carts";
    }


}
