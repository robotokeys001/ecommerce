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
    private UtentiService utentiService;

    @Autowired
    private UsernamePwdAuthenticationProvider authProvider;

    @Bean
    @Order(1) // Aggiungi anche @Order(1) per garantire la precedenza
    SecurityFilterChain adminFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationManager(authManager) // ✅ authManager passato correttamente ora
            .httpBasic(basic ->basic.disable()) // oppure puoi disabilitare se non vuoi basic auth
            .csrf(csrf -> csrf.disable());
    
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/home", "/public/**", "/login", "/register").permitAll()
                .requestMatchers("/utente/**", "/carrello/**").hasRole("UTENTE")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/utente/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(authProvider);
        return builder.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Utenti utente = utentiService.findByNome(username);
            if (utente == null) {
                throw new UsernameNotFoundException("User not found");
            }
            String role = Costanti.ADMIN_ROLE.equals(utente.getRuoli().getNomeRuolo()) ? "ADMIN" : "UTENTE";
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(utente.getPassword())
                    .roles(role)
                    .build();
        };
    }

   
}
