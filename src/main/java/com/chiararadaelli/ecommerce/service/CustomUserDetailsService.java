package com.chiararadaelli.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chiararadaelli.ecommerce.costanti.Costanti;
import com.chiararadaelli.ecommerce.model.Utenti;

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
        // NON aggiungere il prefisso "ROLE_" qui
        String role = Costanti.ADMIN_ROLE.equals(utente.getRuoli().getNomeRuolo()) ? "ADMIN" : "UTENTE";
        return User.withUsername(utente.getEmail())
            .password(utente.getPassword())
            .roles(role) // Spring Security aggiungerà automaticamente "ROLE_"
            .build();
    }
}