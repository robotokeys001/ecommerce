package com.chiararadaelli.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.chiararadaelli.ecommerce.costanti.Costanti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.security.UsernamePwdAuthenticationProvider;
import com.chiararadaelli.ecommerce.service.UtentiService;

// ... (importazioni)

@Configuration
public class SecurityConfiguration {

    @Autowired
    UtentiService utentiService; // Utilizza UtentiService

    @Bean
    SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**") // Applica questa catena solo agli URL che iniziano con /admin/
                .authorizeHttpRequests((authz) -> authz
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.configure(http)) // O formLogin
                        .csrf(csrf -> csrf.disable()); // Rimuovi in produzione
        return http.build();
    }
    
    

    @Bean
    @Order(2)
    SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/", "/home", "/public/**", "/login", "/register").permitAll() // Accesso pubblico
                .requestMatchers("/utente/**", "/carrello/**").hasRole("UTENTE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/utente/", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessUrl("/login?logout=true")
            )
            .csrf(csrf -> csrf.disable()); // Rimuovi in produzione
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Utenti utente = utentiService.findByNome(username); // Utilizza UtentiService
            if (utente == null) {
                throw new UsernameNotFoundException("User with username " + username + " not found.");
            }
           String role = utente.getRuoli().getNomeRuolo().equals(Costanti.ADMIN_ROLE) ? "ADMIN" : "UTENTE";
           return org.springframework.security.core.userdetails.User
        .withUsername(username)
        .password(utente.getPassword())
        .roles(role)
        .build();
        };
    }


    @Autowired
    private UsernamePwdAuthenticationProvider authProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}