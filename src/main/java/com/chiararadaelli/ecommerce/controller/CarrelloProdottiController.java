// package com.chiararadaelli.ecommerce.controller;

// import java.security.Principal;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.chiararadaelli.ecommerce.model.Carrello;
// import com.chiararadaelli.ecommerce.model.Prodotti;
// import com.chiararadaelli.ecommerce.model.Utenti;
// import com.chiararadaelli.ecommerce.service.CarrelloProdottiService;
// import com.chiararadaelli.ecommerce.service.CarrelloService;
// import com.chiararadaelli.ecommerce.service.UtentiService;

// @Controller
// @RequestMapping("/carrello")
// public class CarrelloProdottiController {

//     @Autowired
//     private CarrelloProdottiService carrelloProdottiService;

//     @Autowired
//     private CarrelloService carrelloService;

//     @Autowired
//     UtentiService utentiService;

//     @PostMapping("/aggiungi")
//     public ResponseEntity<String> aggiungiProdotto(@RequestBody Prodotti prodotto, @RequestParam int quantita) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
//             Utenti utente = (Utenti) authentication.getPrincipal();
//             carrelloProdottiService.aggiungiProdottoAlCarrello(utente, prodotto, quantita);
//             return new ResponseEntity<>("Prodotto aggiunto al carrello", HttpStatus.OK);
//         } else {
//             return new ResponseEntity<>("Utente non autenticato", HttpStatus.UNAUTHORIZED);
//         }
//     }

//     @GetMapping
//     public ResponseEntity<List<Prodotti>> visualizzaCarrello() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
//             Utenti utente = (Utenti) authentication.getPrincipal();
//             List<Prodotti> prodottiNelCarrello = carrelloProdottiService.getProdottiNelCarrello(utente); // Passa l'utente al service
//             return new ResponseEntity<>(prodottiNelCarrello, HttpStatus.OK);
//         } else {
//             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//         }
//     }

//     @DeleteMapping("/rimuovi/{prodottoId}")
//     public ResponseEntity<String> rimuoviProdotto(@PathVariable Long prodottoId) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
//             Utenti utente = (Utenti) authentication.getPrincipal();
//             carrelloProdottiService.rimuoviProdottoDalCarrello(utente, prodottoId);
//             return new ResponseEntity<>("Prodotto rimosso dal carrello", HttpStatus.OK);
//         } else {
//             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//         }
//     }

//     @PutMapping("/aggiorna")
//     public ResponseEntity<String> aggiornaQuantita(@RequestParam Long prodottoId, @RequestParam int quantita) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         if (authentication != null && authentication.getPrincipal() instanceof Utenti) {
//             Utenti utente = (Utenti) authentication.getPrincipal();
//             carrelloProdottiService.aggiornaQuantitaProdotto(utente, prodottoId, quantita);
//             return new ResponseEntity<>("Quantità del prodotto aggiornata", HttpStatus.OK);
//         } else {
//             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//         }
//     }
//     @DeleteMapping("/svuota")
// public String svuotaCarrello(Principal principal, RedirectAttributes redirectAttributes) {
//     String email = principal.getName();
//     Utenti utente = utentiService.readByEmail(email);
//     Carrello carrello = carrelloService.findByUtente(utente);
    
//     carrelloService.svuotaCarrello(carrello);
    
//     redirectAttributes.addFlashAttribute("messaggio", "Carrello svuotato con successo!");
//     return "redirect:/utente/carrello";
// }
// }