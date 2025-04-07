package com.chiararadaelli.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.service.CarrelloProdottiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/carrello")
public class CarrelloProdottiController {
    
   

     @Autowired
    private CarrelloProdottiService carrelloProdottiService;

    @PostMapping("/aggiungi")
    public ResponseEntity<String> aggiungiProdotto(@RequestBody Prodotti prodotto) {
        carrelloProdottiService.aggiungiProdotto(prodotto);
        return new ResponseEntity<>("Prodotto aggiunto al carrello", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Prodotti>> visualizzaCarrello() {
        List<Prodotti> prodottiNelCarrello = carrelloProdottiService.getProdottiNelCarrello();
        return new ResponseEntity<>(prodottiNelCarrello, HttpStatus.OK);
    }
}
