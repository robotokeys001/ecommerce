package com.chiararadaelli.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.UtentiRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
      @Autowired
    UtentiRepository utentiRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session) {
        Utenti utente = utentiRepository.readByEmail(authentication.getName());
        model.addAttribute("username", utente.getNome());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        // if(null != utente.getEazyClass() && null != utente.getEazyClass().getName()){
        //     model.addAttribute("enrolledClass", utente.getEazyClass().getName());
        // }
        session.setAttribute("loggedInPerson", utente);
        return "dashboard.html";
    }

}
