package com.chiararadaelli.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chiararadaelli.ecommerce.model.Utenti;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtentiService utentiService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utenti utente = utentiService.findByEmail(username);
        if (utente == null) {
            throw new UsernameNotFoundException("User not found");
        }
        log.info("Utente caricato: {}, Ruolo: {}", utente.getEmail(), utente.getRuoli().getNomeRuolo()); // Log di verifica
        return utente;
    }
}