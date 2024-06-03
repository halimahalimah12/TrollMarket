package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.product.ProductUpsertRequestDto;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("merchandise")
public class ProductController {
    private final ProductService productService;
    private final InfoUserLogin infoUserLogin;

    public ProductController(ProductService productService, InfoUserLogin infoUserLogin) {
        this.productService = productService;
        this.infoUserLogin = infoUserLogin;
    }


    @GetMapping("")
    public ModelAndView index(Model model) {
        ModelAndView mv = new ModelAndView("product/index");
        Seller seller = infoUserLogin.sellerDetailActive();
        mv.addObject("products",productService.getAll(seller));
        if (model.containsAttribute("success")) {
            mv.addObject("success", true);
        }
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @GetMapping("insert")
    public ModelAndView insert() {
        ModelAndView mv = new ModelAndView("product/upsert");
        mv.addObject("dto", ProductUpsertRequestDto.builder().build());
        mv.addObject("categoryDropdown",productService.getCategoryDropdown());
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @PostMapping("upsert")
    private String postInsert(@Valid @ModelAttribute("dto") ProductUpsertRequestDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDropdown",productService.getCategoryDropdown());

            return "product/upsert";
        }
        Seller seller = infoUserLogin.sellerDetailActive();
        productService.save(dto,seller.getId());
        redirectAttributes.addFlashAttribute("success",true);

        return "redirect:/merchandise";
    }

    @GetMapping("update/{id}")
    public ModelAndView update(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("product/upsert");
        mv.addObject("dto", productService.getProductById(id));
        mv.addObject("categoryDropdown",productService.getCategoryDropdown());
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }



}
