package com.chiararadaelli.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chiararadaelli.ecommerce.costanti.Costanti;
import com.chiararadaelli.ecommerce.model.Ruoli;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.RuoliService;
import com.chiararadaelli.ecommerce.service.UtentiService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("public")
public class PublicController {
    
    @Autowired
    RuoliService ruoliService;

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
        boolean isSaved = utentiService.createUser(utente);
        if(isSaved){
            return "redirect:/login?register=true";
        }else {
            return "register.html";
        }
    }
 @RequestMapping(value = "newuserregister", method = RequestMethod.POST)
    public ModelAndView newUseRegister(@ModelAttribute Utenti utente, RedirectAttributes redirectAttributes) {
        try {
            if (!utentiService.existsByNome(utente.getNome())) { // Usa un metodo dedicato nel service
                Ruoli ruoloUtente = ruoliService.getRuoloByNome(Costanti.UTENTE_ROLE);
                if (ruoloUtente != null) {
                    utente.setRuoli(ruoloUtente);
                    utentiService.createUser(utente); // La codifica della password dovrebbe avvenire qui
                    redirectAttributes.addFlashAttribute("successMessage", "Utente registrato con successo.");
                    return new ModelAndView("userLogin");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Errore durante la registrazione: Ruolo utente non trovato.");
                    return new ModelAndView("register");
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", utente.getNome() + " is taken. Please choose a different username.");
                return new ModelAndView("register");
            }
        } catch (Exception e) {
            log.error("Error during user registration", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error during user registration.");
            return new ModelAndView("register");
        }
    }

    @GetMapping("/profileDisplay")
    public String profileDisplay(Model model, HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utenti utente = utentiService.findByNome(username);

        if (utente != null) {
            model.addAttribute("userid", utente.getUtentiId());
            model.addAttribute("username", utente.getNome());
            model.addAttribute("email", utente.getEmail());
            // Non passare mai la password al template
        } else {
            model.addAttribute("msg", "User not found");
        }
        return "updateProfile";
    }

}
