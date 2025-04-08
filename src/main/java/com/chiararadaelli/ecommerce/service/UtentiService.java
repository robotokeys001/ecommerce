package com.chiararadaelli.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chiararadaelli.ecommerce.costanti.Costanti;
import com.chiararadaelli.ecommerce.model.Ruoli;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.RuoliRepository;
import com.chiararadaelli.ecommerce.repository.UtentiRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UtentiService {
    
    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private RuoliRepository ruoliRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean createUser(Utenti utente) {
        try {
            Ruoli ruolo = ruoliRepository.findByNomeRuolo(Costanti.UTENTE_ROLE);
            if (ruolo == null) {
                throw new IllegalStateException("Ruolo predefinito non trovato: " + Costanti.UTENTE_ROLE);
            }
            utente.setRuoli(ruolo);
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            Utenti savedUtente = utentiRepository.save(utente);
            return savedUtente.getUtentiId() != null && savedUtente.getUtentiId() > 0;
        } catch (IllegalStateException e) {
            log.error("Errore durante la creazione dell'utente: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Errore inatteso durante la creazione dell'utente", e);
            return false;
        }
    }

    public List<Utenti> getAllUtenti() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUtenti'");
    }

    public Utenti readByEmail(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readByEmail'");
    }

    public Utenti findByNome(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNome'");
    }

    public boolean existsByNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByNome'");
    }

    // ... altri metodi

}
