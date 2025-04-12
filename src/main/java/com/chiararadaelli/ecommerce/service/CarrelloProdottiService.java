package com.chiararadaelli.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.CarrelloProdottiRepository;
import com.chiararadaelli.ecommerce.repository.ProdottiRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarrelloProdottiService {

    @Autowired
    private CarrelloProdottiRepository carrelloProdottiRepo;

    @Autowired
    private ProdottiRepository prodottiRepository;

    @Autowired
    private CarrelloService carrelloService;

    @Transactional // Assicura che l'operazione sia atomica
    public void aggiungiProdottoAlCarrello(Utenti utente, Prodotti prodotto, int quantita) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        if (carrello == null) {
            // Crea un nuovo carrello per l'utente se non esiste
            carrello = new Carrello();
            carrello.setUtente(utente);
            carrelloService.salvaCarrello(carrello); // Assicurati di avere questo metodo nel tuo CarrelloService
        }

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

    public Prodotti getProdottoById(Long prodottoId) {
        Optional<Prodotti> prodottoOptional = prodottiRepository.findById(prodottoId);
        return prodottoOptional.orElse(null); // Restituisce null se il prodotto non viene trovato
    }

    public List<CarrelloProdotti> getCarrelloProdottiByUtente(Utenti utente) {
        Carrello carrello = carrelloService.getCarrelloByUtente(utente);
        if (carrello != null) {
            log.info("CarrelloProdottiService - Carrello trovato per l'utente: {}", utente.getEmail()); // LOG AGGIUNTO
            List<CarrelloProdotti> carrelloProdotti = carrelloProdottiRepo.findByCarrelloWithProdotti(carrello);
            log.info("CarrelloProdottiService - Numero di CarrelloProdotti trovati: {}", carrelloProdotti.size()); // LOG AGGIUNTO
            return carrelloProdotti;
        } else {
            log.warn("CarrelloProdottiService - Nessun carrello trovato per l'utente: {}", utente.getEmail()); // LOG AGGIUNTO
            return new ArrayList<>();
        }
    }


    // ... altri metodi per rimuovere, aggiornare la quantità, ecc.
}