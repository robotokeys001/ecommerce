package com.chiararadaelli.ecommerce.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chiararadaelli.ecommerce.model.Utenti;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;



@Controller
@Slf4j
@RequestMapping("utente")
public class UtenteController {

      @GetMapping("/displayCarrello")
    public ModelAndView displayCourses(Model model, HttpSession session) {
        Utenti utente = (Utenti) session.getAttribute("loggedInPerson");
        ModelAndView modelAndView = new ModelAndView("carrello.html");
        modelAndView.addObject("utente",utente);
        return modelAndView;
    }


    
}
