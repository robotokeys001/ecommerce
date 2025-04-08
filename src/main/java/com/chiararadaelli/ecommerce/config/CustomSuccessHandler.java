package com.chiararadaelli.ecommerce.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println("Il CustomSuccessHandler è stato chiamato!"); // AGGIUNGI QUESTA LINEA

        System.out.println("Ruoli dell'utente autenticato: " + authentication.getAuthorities());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String ruolo = authority.getAuthority();

            if (ruolo.equals("ROLE_ADMIN")) {
               response.sendRedirect("/admin/dashboard");
              
                return;
            } else if (ruolo.equals("ROLE_UTENTE")) {
                response.sendRedirect("/utente/dashboard");
                return;
            }
        }

        // fallback
        response.sendRedirect("/403");
    }
}