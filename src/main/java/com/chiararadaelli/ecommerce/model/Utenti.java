package com.chiararadaelli.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Data
public class Utenti implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long utentiId;

    private String nome;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "ruoli_id")
    private Ruoli ruoli;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assicurati che 'ruoli' non sia null
        if (ruoli == null || ruoli.getNomeRuolo() == null) {
            return Collections.emptyList();
        }
        // Restituisci il ruolo con il prefisso "ROLE_"
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + ruoli.getNomeRuolo()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //logica per verificare se l'account è scaduto
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // logica per verificare se l'account è bloccato
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // logica per verificare se le credenziali sono scadute
    }

    @Override
    public boolean isEnabled() {
        return true; // logica per verificare se l'utente è abilitato
    }
}