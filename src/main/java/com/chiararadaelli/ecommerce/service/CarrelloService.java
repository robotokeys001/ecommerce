package com.chiararadaelli.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.CarrelloProdottiRepository;
import com.chiararadaelli.ecommerce.repository.CarrelloRepository;

import jakarta.transaction.Transactional;

@Service
public class CarrelloService {
 @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    CarrelloProdottiRepository carrelloProdottiRepository;

    @Transactional
    public Carrello creaCarrelloPerUtente(Utenti utente) {
        Carrello carrello = new Carrello();
        carrello.setUtente(utente);
        return carrelloRepository.save(carrello);
    }

    public Carrello getCarrelloByUtente(Utenti utente) {
        return carrelloRepository.findByUtente(utente);
    }

    @Transactional
    public void eliminaCarrello(Carrello carrello) {
        carrelloRepository.delete(carrello);
    }

    @Transactional
    public void svuotaCarrello(Carrello carrello) {
        // Elimina tutti gli elementi CarrelloProdotti associati al carrello
        List<CarrelloProdotti> carrelloProdotti = carrelloProdottiRepository.findByCarrello(carrello);
        carrelloProdottiRepository.deleteAll(carrelloProdotti);
        // Non è necessario salvare il carrello stesso, poiché rimane vuoto
    }

    public Carrello findByUtente(Utenti utente) {
        return carrelloRepository.findByUtente(utente);
    }
    
  
    // Altri metodi per gestire il carrello (svuotamento, ecc.)
}

