package com.chiararadaelli.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.service.ProdottiService;

@Controller
public class ProdottoViewController { // Un nome più specifico per il controller

    @Autowired
    ProdottiService prodottiService;

    @GetMapping("/listaprodotti")
    public ModelAndView mostraListaProdotti() {
        List<Prodotti> prodotti = prodottiService.findAll();
        ModelAndView mv = new ModelAndView("listaprodotti.html");
        mv.addObject("products", prodotti);
        return mv;
    }
}