package com.chiararadaelli.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.ProdottiService;
import com.chiararadaelli.ecommerce.service.RuoliService;
import com.chiararadaelli.ecommerce.service.UtentiService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("utente")
public class UtenteController {

    @Autowired
    ProdottiService prodottiService;

    @Autowired
    UtentiService utentiService;

    @Autowired
    RuoliService ruoliService; // Assicurati che RuoliRepository sia autowired

    @GetMapping("/displayCarrello")
    public ModelAndView displayCarrello(Model model, HttpSession session) {
        Utenti utente = (Utenti) session.getAttribute("loggedInPerson");
        ModelAndView modelAndView = new ModelAndView("carrello.html");
        modelAndView.addObject("utente", utente);
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView indexPage() {
        ModelAndView mView = new ModelAndView("index");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        mView.addObject("username", username);
        List<Prodotti> prodotti = prodottiService.findAll(); // Assumi un metodo nel tuo service

        if (prodotti.isEmpty()) {
            mView.addObject("msg", "No products are available");
        } else {
            mView.addObject("products", prodotti);
        }
        return mView;
    }
   
   
}