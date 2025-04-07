package com.chiararadaelli.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.UtentiService;


import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("public")
public class PublicController {
    

      @Autowired
    UtentiService utentiService;

    @RequestMapping(value ="/register",method = { RequestMethod.GET})
    public String displayRegisterPage(Model model) {
        model.addAttribute("utente", new Utenti());
        return "register.html";
    }

    @RequestMapping(value ="/createUser",method = { RequestMethod.POST})
    public String createUser(@ModelAttribute("utente") Utenti utente, Errors errors) {
        if(errors.hasErrors()){
            return "register.html";
        }
        boolean isSaved = utentiService.createNewPerson(utente);
        if(isSaved){
            return "redirect:/login?register=true";
        }else {
            return "register.html";
        }
    }


}
