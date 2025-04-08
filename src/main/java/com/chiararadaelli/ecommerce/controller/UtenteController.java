package com.chiararadaelli.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("/dashboard")
public String dashboard(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Authentication object in dashboard: {}", authentication);
    if (authentication != null) {
        log.info("Principal object in dashboard: {}", authentication.getPrincipal());
        model.addAttribute("principalObject", authentication.getPrincipal());
    } else {
        log.warn("Authentication object is null in dashboard!");
        model.addAttribute("principalObject", null);
    }
    return "utenteDashboard";
}
    @GetMapping("/displayCarrello")
    public ModelAndView displayCarrello(Principal principal) {
        Utenti utente = utentiService.readByEmail(principal.getName());
        ModelAndView modelAndView = new ModelAndView("carrello.html");
        modelAndView.addObject("utente", utente);
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView homePage() {
        ModelAndView mView = new ModelAndView("home");
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
   
   @GetMapping("/modifica-profilo")
public String mostraFormProfilo(Model model, Principal principal) {
    Utenti utente = utentiService.readByEmail(principal.getName());
    model.addAttribute("utente", utente);
    return "/modifica-profilo";
}
@PostMapping("/modifica-profilo")
public String aggiornaProfilo(@ModelAttribute("utente") Utenti utenteModificato, Principal principal, RedirectAttributes redirectAttributes) {
    Utenti utente = utentiService.readByEmail(principal.getName());

    utente.setNome(utenteModificato.getNome());
    utente.setEmail(utenteModificato.getEmail());

    utentiService.salvaUtente(utente);
    redirectAttributes.addFlashAttribute("messaggio", "Profilo aggiornato con successo!");
    return "redirect:/utente/dashboard";
}
//     @GetMapping("/listaprodotti")
//     public ModelAndView mostraListaProdotti() {
//         List<Prodotti> prodotti = prodottiService.findAll();
//         ModelAndView mv = new ModelAndView("listaprodotti.html");
//         mv.addObject("products", prodotti);
//         return mv;
// }

}