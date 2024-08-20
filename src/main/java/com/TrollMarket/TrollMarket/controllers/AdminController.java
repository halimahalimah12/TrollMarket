package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.ExcelGenerator;
import com.TrollMarket.TrollMarket.ExcelUtility;
import com.TrollMarket.TrollMarket.InfoUserLogin;
import com.TrollMarket.TrollMarket.dtos.UploadFileToDbDto;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderRowDto;
import com.TrollMarket.TrollMarket.dtos.order.OrderSearchDto;
import com.TrollMarket.TrollMarket.services.AuthService;
import com.TrollMarket.TrollMarket.services.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Console;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=HistoryOrder" + currentDateTime +".xlsx";
        response.setHeader(headerKey,headerValue);

        List<OrderRowDto> listOfOrder = orderService.getAllOrderExcel();
        ExcelGenerator generator = new ExcelGenerator(listOfOrder);
        generator.generateExcelFile(response);

    }

    @PostMapping("/excel/upload")
    public String uploadFile(UploadFileToDbDto file){
     String message ="";
     var fileExcel = file.getFile();
     if(ExcelUtility.hasExcelFormat(fileExcel)){
         try {
             orderService.saveDataFromExcel(fileExcel);
             message = "The Excel file is uploaded"+fileExcel.getOriginalFilename();

         }catch (Exception ex){
             ex.printStackTrace();
             message = "an error"+ex.getMessage();
         }
     }
     return "redirect:/history";
    }
}
