package com.TrollMarket.TrollMarket.controllers;

import com.TrollMarket.TrollMarket.services.ShipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("shipments")
public class ShipmentController {
    private ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("shipment/index");
        mv.addObject("shipments",shipmentService.getAll());
        return mv;
    }
}
