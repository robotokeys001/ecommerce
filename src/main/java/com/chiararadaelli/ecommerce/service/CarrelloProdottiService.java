package com.chiararadaelli.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.CarrelloProdottiRepository;

@Service
public class CarrelloProdottiService {
    
    @Autowired
    private CarrelloProdottiRepository carrelloProdottiRepo;

        public List<CarrelloProdotti> listaCarrelloProdotti(Utenti utenti){
            return carrelloProdottiRepo.findByUtenti(utenti);
        }
            private List<Prodotti> prodotti = new ArrayList<>();

        public void aggiungiProdotto(Prodotti prodotto) {
            prodotti.add(prodotto);
        }

        public List<Prodotti> getProdottiNelCarrello() {
            return prodotti;
        }

        public void rimuoviProdotto(Prodotti prodotto) {
            prodotti.remove(prodotto);
        
    }
}
