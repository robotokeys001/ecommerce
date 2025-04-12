package com.chiararadaelli.ecommerce.controller;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.CarrelloProdottiService;
import com.chiararadaelli.ecommerce.service.CarrelloService;
import com.chiararadaelli.ecommerce.service.UtentiService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/carrello")
public class CarrelloController {

    private final CarrelloProdottiService carrelloProdottiService;

    public CarrelloController(CarrelloProdottiService carrelloProdottiService) {
        this.carrelloProdottiService = carrelloProdottiService;
    }
    @Autowired
    CarrelloService carrelloService;

    @Autowired
    UtentiService utentiService;

    @GetMapping("/display")
    public ModelAndView displayCarrelloView(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("carrello");
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            log.info("CarrelloController - Utente autenticato: {}", utente.getEmail());
            List<CarrelloProdotti> carrelloItems = carrelloProdottiService.getCarrelloProdottiByUtente(utente);
            log.info("CarrelloController - Numero di elementi nel carrello recuperati: {}", carrelloItems.size());
            modelAndView.addObject("carrelloItems", carrelloItems);
            modelAndView.addObject("utente", utente);
        } else {
            log.warn("CarrelloController - Utente non autenticato o Principal non è un'istanza di Utenti.");
        }
        return modelAndView;
    }
    
    // @PostMapping("/aggiungi")
    // public ResponseEntity<String> aggiungiProdotto(@RequestBody Prodotti prodotto, @RequestParam int quantita) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
    //         Utenti utente = (Utenti) authentication.getPrincipal();
    //         carrelloProdottiService.aggiungiProdottoAlCarrello(utente, prodotto, quantita);
    //         return new ResponseEntity<>("Prodotto aggiunto al carrello", HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>("Utente non autenticato", HttpStatus.UNAUTHORIZED);
    //     }
    // }
    @PostMapping("/aggiungi")
    public String aggiungiProdotto(@RequestParam("prodottoId") Long prodottoId, @RequestParam("quantita") int inventario, Principal principal, RedirectAttributes redirectAttributes) {
        log.info("Tentativo di aggiungere il prodotto con ID: {} e inventario: {}", prodottoId, inventario);
        if (principal != null) {
            String email = principal.getName();
            log.info("Utente autenticato: {}", email);
            Utenti utente = utentiService.readByEmail(email);
            log.info("Utente trovato: {}", utente);
            Prodotti prodottoDaAggiungere = carrelloProdottiService.getProdottoById(prodottoId);
            log.info("Prodotto trovato: {}", prodottoDaAggiungere);
    
            if (prodottoDaAggiungere != null) {
                carrelloProdottiService.aggiungiProdottoAlCarrello(utente, prodottoDaAggiungere, inventario);
                log.info("Prodotto aggiunto al carrello.");
                redirectAttributes.addFlashAttribute("messaggio", prodottoDaAggiungere.getNomeProdotto() + " aggiunto al carrello!");
                return "redirect:/public/listaprodotti";
            } else {
                log.warn("Prodotto con ID {} non trovato.", prodottoId);
                redirectAttributes.addFlashAttribute("errore", "Prodotto non trovato.");
                return "redirect:/public/listaprodotti";
            }
        } else {
            log.warn("Utente non autenticato.");
            redirectAttributes.addFlashAttribute("errore", "Utente non autenticato.");
            return "redirect:/login";
        }
    }
    @GetMapping // URL base /carrello per visualizzare i prodotti nel carrello come API
    public ResponseEntity<List<Prodotti>> visualizzaCarrelloAPI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            List<Prodotti> prodottiNelCarrello = carrelloProdottiService.getProdottiNelCarrello(utente);
            return new ResponseEntity<>(prodottiNelCarrello, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/rimuovi/{prodottoId}")
    public ResponseEntity<String> rimuoviProdotto(@PathVariable Long prodottoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            carrelloProdottiService.rimuoviProdottoDalCarrello(utente, prodottoId);
            return new ResponseEntity<>("Prodotto rimosso dal carrello", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<String> aggiornaQuantita(@RequestParam Long prodottoId, @RequestParam int quantita) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
            Utenti utente = (Utenti) authentication.getPrincipal();
            carrelloProdottiService.aggiornaQuantitaProdotto(utente, prodottoId, quantita);
            return new ResponseEntity<>("Quantità del prodotto aggiornata", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/svuota")
    public String svuotaCarrello(Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        Utenti utente = utentiService.readByEmail(email);
        Carrello carrello = carrelloService.findByUtente(utente);

        carrelloService.svuotaCarrello(carrello);

        redirectAttributes.addFlashAttribute("messaggio", "Carrello svuotato con successo!");
        return "redirect:/utente/carrello";
    }
}