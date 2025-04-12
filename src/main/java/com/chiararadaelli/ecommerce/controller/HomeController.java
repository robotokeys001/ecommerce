package com.chiararadaelli.ecommerce.controller;

import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.service.ProdottiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProdottiService prodottoService; // Inietta il tuo servizio per i prodotti

    @RequestMapping(value = {"","/","home"})
    public String displayHomePage(Model model) {
        // Recupera l'username dell'utente loggato (se presente)
        String username = null;
        // ... (la tua logica per ottenere l'username)
        model.addAttribute("username", username);

        // Recupera eventuali messaggi flash
        String messaggioFlash = null;
        // ... (la tua logica per i messaggi flash)
        model.addAttribute("msg", messaggioFlash);

        // Recupera i prodotti per la homepage utilizzando il tuo servizio
        List<Prodotti> prodottiHomepage = prodottoService.findTop5ProdottiPerHomepage();
        model.addAttribute("products", prodottiHomepage); // Aggiungi la lista al model

        return "home";
    }
}