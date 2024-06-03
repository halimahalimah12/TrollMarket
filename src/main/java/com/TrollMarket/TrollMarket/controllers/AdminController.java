package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderSearchDto;
import com.TrollMarket.TrollMarket.services.AuthService;
import com.TrollMarket.TrollMarket.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final AuthService authService;
    private final InfoUserLogin infoUserLogin;

    private final OrderService orderService;

    public AdminController(AuthService authService, InfoUserLogin infoUserLogin, OrderService orderService) {
        this.authService = authService;
        this.infoUserLogin = infoUserLogin;
        this.orderService = orderService;
    }

    @GetMapping("admin")
    public  ModelAndView admin(Model model){
        var mv = new ModelAndView("admin/admin");
        mv.addObject("dto", AuthRegisterDto.builder().build());
        if (model.containsAttribute("success")) {
            mv.addObject("success", true);
        }
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @PostMapping("addAdmin")
    public String addAdmin(@Valid @ModelAttribute("dto") AuthRegisterDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/admin";
        }
        authService.register(dto);
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/admin";
    }

    @GetMapping("history")
    public ModelAndView history(OrderSearchDto dto){
        var mv = new ModelAndView("admin/history");
        infoUserLogin.userDetailLogin(mv);
        mv.addObject("orders",orderService.getAll(dto));
        mv.addObject("dto",dto);
        mv.addObject("sellerDropdown",orderService.getSellerDropdown());
        mv.addObject("buyerDropdown",orderService.getBuyerDropdown());

        return mv;
    }

}
