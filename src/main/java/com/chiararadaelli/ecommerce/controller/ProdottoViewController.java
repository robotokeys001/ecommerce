package com.chiararadaelli.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.service.ProdottiService;

@Controller
@RequestMapping("/public")
public class ProdottoViewController { // Un nome più specifico per il controller

    @Autowired
    ProdottiService prodottiService;

    @GetMapping("/listaprodotti")
    public ModelAndView mostraListaProdotti() {
        List<Prodotti> prodotti = prodottiService.findAll();
        ModelAndView mv = new ModelAndView("listaprodotti");
        mv.addObject("products", prodotti);
        return mv;
    }

    @GetMapping("/prodotto/{id}") // Corretto l'URL per coerenza con il template
    public String visualizzaDettagliProdotto(@PathVariable Long id, Model model) {
        Optional<Prodotti> prodottoOptional = prodottiService.getProdottoById(id); // Assicurati di avere questo metodo nel tuo service

        if (prodottoOptional.isPresent()) {
            model.addAttribute("prodotto", prodottoOptional.get()); // Passa l'oggetto Prodotti al model
            return "dettagliProdotto"; // Nome del template per i dettagli del prodotto
        } else {
            // Gestisci il caso in cui il prodotto non viene trovato
            return "error/prodotto-non-trovato"; // Crea una pagina di errore appropriata
        }
    }
}