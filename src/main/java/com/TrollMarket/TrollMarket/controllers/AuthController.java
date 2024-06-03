package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterSellerBuyerDto;
import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.services.AccountService;
import com.TrollMarket.TrollMarket.services.AuthService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {
    private final AuthService authService;
    private final AccountService accountService;
    private final InfoUserLogin infoUserLogin;

    public AuthController(AuthService authService, AccountService accountService, InfoUserLogin infoUserLogin) {
        this.authService = authService;
        this.accountService = accountService;
        this.infoUserLogin = infoUserLogin;
    }

    @GetMapping("registerSeller")
    public ModelAndView registerSeler() {
        var mv = new ModelAndView("auth/registerSeller");
        mv.addObject("dto", AuthRegisterSellerBuyerDto.builder().build());
        return mv;

    }

    @PostMapping("registerSeller")
    public String registerSeller(@Valid @ModelAttribute("dto") AuthRegisterSellerBuyerDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/registerSeller";
        }
        authService.registerSeller(dto);
        redirectAttributes.addFlashAttribute("success",true);

        return "redirect:/login";
    }

    @GetMapping("registerBuyer")
    public ModelAndView registerBuyer() {
        var mv = new ModelAndView("auth/registerBuyer");
        mv.addObject("dto", AuthRegisterSellerBuyerDto.builder().build());
        return mv;

    }

    @PostMapping("registerBuyer")
    public String registerBuyer(@Valid @ModelAttribute("dto") AuthRegisterSellerBuyerDto dto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/registerBuyer";
        }
        authService.registerBuyer(dto);
        redirectAttributes.addFlashAttribute("success",true);

        return "redirect:/login";
    }

    @GetMapping("login")
    public ModelAndView login(@RequestParam(required = false) Boolean error, Model model) {
        ModelAndView mv = new ModelAndView("/auth/login");
        if (model.containsAttribute("success")) {
            mv.addObject("success", true);
        }
        mv.addObject("error", error);
        return mv;
    }

    @GetMapping("")
    public ModelAndView redirectAfterLogin(Authentication authentication) {
        String username = null;
        if (authentication != null) {
            username = authentication.getName();
        }

        ModelAndView mv = new ModelAndView("auth/selectRole");
        mv.addObject("roles", accountService.getRoleDropdown(username));
        return mv;
    }

    @PostMapping("role")
    public String role(String roleName) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), List.of(new SimpleGrantedAuthority(roleName)));

        securityContext.setAuthentication(newAuthentication);

        return "redirect:/home";
    }

    @GetMapping("home")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        infoUserLogin.userDetailLogin(mv);
        return mv;
    }


}