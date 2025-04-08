package com.chiararadaelli.ecommerce.service;

import java.util.List;
import java.util.Optional;

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
    

    private final UtentiRepository utentiRepository;
    private final RuoliRepository ruoliRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtentiService(UtentiRepository utentiRepository, RuoliRepository ruoliRepository, PasswordEncoder passwordEncoder) {
        this.utentiRepository = utentiRepository;
        this.ruoliRepository = ruoliRepository;
        this.passwordEncoder = passwordEncoder;
    }
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
        return utentiRepository.findAll();
    }
    
    public Utenti readByEmail(String email) {
        Optional<Utenti> utente = utentiRepository.findByEmail(email);
        return utente.orElse(null); // oppure lancia una custom exception se preferisci
    }
    
    public Utenti findByNome(String username) {
        Optional<Utenti> utente = utentiRepository.findByNome(username);
        return utente.orElse(null);
    }
    
    public boolean existsByNome(String nome) {
        return utentiRepository.existsByNome(nome);
    }
    public void salvaUtente(Utenti utente) {
        // Controllo se l'utente è nuovo (senza ID) o esistente
        boolean isNuovo = (utente.getUtentiId() == null);
    
        // Se la password è già cifrata o vuota, non ricifrare
        if (isNuovo || utente.getPassword().length() < 60) { // BCrypt ha 60 caratteri
            String encodedPassword = passwordEncoder.encode(utente.getPassword());
            utente.setPassword(encodedPassword);
        }
        if (utentiRepository.existsByEmail(utente.getEmail())) {
            throw new RuntimeException("Email già in uso");
        }
    
        utentiRepository.save(utente);
    }

    // ... altri metodi

}
