package com.chiararadaelli.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.CarrelloProdottiRepository;

@Service
public class CarrelloProdottiService {

    @Autowired
    private CarrelloProdottiRepository carrelloProdottiRepo;

    @Autowired
    private CarrelloService carrelloService; // Assumendo che esista

    public void aggiungiProdottoAlCarrello(Utenti utente, Prodotti prodotto, int quantita) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        if (carrello != null) {
            CarrelloProdotti esistente = carrelloProdottiRepo.findByCarrelloAndProdotti(carrello, prodotto);
            if (esistente != null) {
                esistente.setQuantita(esistente.getQuantita() + quantita);
                carrelloProdottiRepo.save(esistente);
            } else {
                CarrelloProdotti nuovoArticolo = new CarrelloProdotti();
                nuovoArticolo.setCarrello(carrello);
                nuovoArticolo.setProdotti(prodotto);
                nuovoArticolo.setQuantita(quantita);
                carrelloProdottiRepo.save(nuovoArticolo);
            }
        } else {
            // Gestire il caso in cui l'utente non ha un carrello (potrebbe crearne uno)
        }
    }

    public List<CarrelloProdotti> getCarrelloProdottiByUtente(Utenti utente) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        if (carrello != null) {
            return carrelloProdottiRepo.findByCarrello(carrello);
        }
        return new ArrayList<>(); // O gestisci il caso di carrello nullo come preferisci
    }

    @Autowired
    private ProdottiService prodottiService; // Assicurati che sia autowired
    
    public void rimuoviProdottoDalCarrello(Utenti utente, Long prodottoId) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        Prodotti prodotto = prodottiService.getProdottoById(prodottoId).orElse(null);
        if (carrello != null && prodotto != null) {
            CarrelloProdotti esistente = carrelloProdottiRepo.findByCarrelloAndProdotti(carrello, prodotto);
            if (esistente != null) {
                carrelloProdottiRepo.delete(esistente);
            }
        }
        // Potresti voler gestire il caso in cui il prodotto non è nel carrello
    }

    public void aggiornaQuantitaProdotto(Utenti utente, Long prodottoId, int quantita) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        Prodotti prodotto = prodottiService.getProdottoById(prodottoId).orElse(null);
        if (carrello != null && prodotto != null) {
            CarrelloProdotti esistente = carrelloProdottiRepo.findByCarrelloAndProdotti(carrello, prodotto);
            if (esistente != null) {
                esistente.setQuantita(quantita);
                carrelloProdottiRepo.save(esistente);
            }
            // Potresti voler gestire il caso in cui il prodotto non è nel carrello
        }
    }

    public List<Prodotti> getProdottiNelCarrello(Utenti utente) {
        List<CarrelloProdotti> carrelloProdotti = getCarrelloProdottiByUtente(utente);
        List<Prodotti> prodottiNelCarrello = new ArrayList<>();
        for (CarrelloProdotti cp : carrelloProdotti) {
            prodottiNelCarrello.add(cp.getProdotti());
        }
        return prodottiNelCarrello;
    }
  

    // ... altri metodi per rimuovere, aggiornare la quantità, ecc.
}
