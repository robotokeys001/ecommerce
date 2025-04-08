package com.chiararadaelli.ecommerce.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService; // Inietta CustomUserDetailsService

    public UsernamePwdAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("Tentativo di login per utente: {}", email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            log.warn("Login fallito: utente non trovato con email {}", email);
            throw new BadCredentialsException("Credenziali non valide!");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.warn("Login fallito: password errata per utente {}", email);
            log.info("Password inserita: {}", password);
            log.info("Password salvata (hash): {}", userDetails.getPassword());
            log.info("Match? {}", passwordEncoder.matches(password, userDetails.getPassword()));
            throw new BadCredentialsException("Credenziali non valide!");
        }

        log.info("Login riuscito per utente: {}", email);
        return new UsernamePasswordAuthenticationToken(
            userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}