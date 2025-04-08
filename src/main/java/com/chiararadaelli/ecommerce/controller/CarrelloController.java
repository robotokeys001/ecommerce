package com.chiararadaelli.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chiararadaelli.ecommerce.model.CarrelloProdotti;

import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.CarrelloProdottiService;


@Controller
public class CarrelloController {


    @Autowired
    CarrelloProdottiService carrelloProdottiService;

    @GetMapping("/displayCarrello")
    public ModelAndView displayCarrello() {
        ModelAndView modelAndView = new ModelAndView("carrello.html");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            List<CarrelloProdotti> carrelloItems = carrelloProdottiService.getCarrelloProdottiByUtente(utente);
            modelAndView.addObject("carrelloItems", carrelloItems);
            modelAndView.addObject("utente", utente); // Potrebbe essere necessario per altre info
        }
        return modelAndView;
    }

    @PostMapping("/carrello/aggiungi-prodotto")
    public ResponseEntity<String> aggiungiProdottoAlCarrello(@RequestParam("prodottoId") Long prodottoId, @RequestParam("quantita") int quantita) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            carrelloProdottiService.aggiungiProdottoAlCarrello(utente, null, quantita);
            return new ResponseEntity<>("Prodotto aggiunto al carrello", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Utente non autenticato", HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/carrello/rimuovi-dal-carrello/{prodottoId}")
    public ResponseEntity<String> rimuoviProdottoDalCarrello(@PathVariable Long prodottoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            carrelloProdottiService.rimuoviProdottoDalCarrello(utente, prodottoId);
            return new ResponseEntity<>("Prodotto rimosso dal carrello", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/carrello/aggiorna-quantita")
    public ResponseEntity<String> aggiornaQuantitaCarrello(@RequestParam("prodottoId") Long prodottoId, @RequestParam("quantita") int quantita) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            carrelloProdottiService.aggiornaQuantitaProdotto(utente, prodottoId, quantita);
            return new ResponseEntity<>("Quantità del carrello aggiornata", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

   
}
