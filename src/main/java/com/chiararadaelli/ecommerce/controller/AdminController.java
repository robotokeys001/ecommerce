package com.chiararadaelli.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.repository.ProdottiRepository;
import com.chiararadaelli.ecommerce.repository.UtentiRepository;
import ch.qos.logback.core.model.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {
   

    @Autowired
    UtentiRepository utentiRepository;

    @Autowired
    ProdottiRepository prodottiRepository;

    @RequestMapping("/displayProdotti")
    public ModelAndView displayProdotti(Model model) {
        List<Prodotti> prodotto = prodottiRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("prodotti.html");
        modelAndView.addObject("prodotti",prodotto);
        modelAndView.addObject("prodotto", new Prodotti());
        return modelAndView;
    }

    @PostMapping("/addNewProdotto")
    public ModelAndView addNewProdotto(Model model, @ModelAttribute("prodotto") Prodotti prodotto) {
        prodottiRepository.save(prodotto);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayProdotti");
        return modelAndView;
    }

    @RequestMapping("/deleteProdotto")
    public ModelAndView deleteClass(Model model, @RequestParam Long id) {
        prodottiRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayProdotti");
        return modelAndView;
    }


}
