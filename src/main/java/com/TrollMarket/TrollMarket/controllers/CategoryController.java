package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.category.CategoryDeleteItemDto;
import com.TrollMarket.TrollMarket.dtos.category.CategorySearchDto;
import com.TrollMarket.TrollMarket.dtos.category.CategoryUpsertRequestDto;
import com.TrollMarket.TrollMarket.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final InfoUserLogin infoUserLogin;

    public CategoryController(CategoryService categoryService, InfoUserLogin infoUserLogin) {
        this.categoryService = categoryService;
        this.infoUserLogin = infoUserLogin;
    }

    @GetMapping("")
    public ModelAndView index(CategorySearchDto dto,Model model) {
        ModelAndView mv = new ModelAndView("category/index");
        mv.addObject("categories",categoryService.getAll(dto));
        mv.addObject("dto",dto);
        if (model.containsAttribute("success")) {
            mv.addObject("success", true);
        }
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @GetMapping("insert")
    public ModelAndView insert(CategoryUpsertRequestDto dto){
        ModelAndView mv = new ModelAndView("category/upsert");
        mv.addObject("dto", CategoryUpsertRequestDto.builder().build());
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }
    @PostMapping("upsert")
    private String postInsert(@Valid @ModelAttribute("dto") CategoryUpsertRequestDto dto, BindingResult bindingResult , RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "category/upsert";
        }
        categoryService.save(dto);
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/categories";
    }

    @GetMapping("update/{id}")
    public  ModelAndView update(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("category/upsert");
        mv.addObject("dto",categoryService.getById(id));
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @GetMapping("delete/{id}")
    public  ModelAndView delete(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("category/delete");
        mv.addObject("category",categoryService.getItemByIdForDelete(id));
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }

    @PostMapping("delete")
    public String postDelete(CategoryDeleteItemDto dto, Model model) {
        Integer dependencies = categoryService.getTotalProduct(dto.getId());
        if (dependencies == 0) {
            categoryService.deleteById(dto.getId());
            return "redirect:/categories";
        } else {
            model.addAttribute("dependencies",dependencies);
            return "category/delete-failed";
        }
    }
}
