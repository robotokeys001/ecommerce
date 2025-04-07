package com.chiararadaelli.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/saveMsg").ignoringRequestMatchers("/public/**"))
             .authorizeHttpRequests(auth -> auth
             .requestMatchers("/displayMessages").hasRole("ADMIN")
             .requestMatchers("addProdotti").hasRole("ADMIN")
             .requestMatchers("/dashboard").authenticated()
             .requestMatchers("/login").permitAll()
             .requestMatchers("/public/**").permitAll()//dice a spring che tutti i path con il prefisso
            //     .requestMatchers("/admin/**").hasRole("ROLE_ADMIN") // /admin/** richiede ruolo ADMIN
            //     .requestMatchers("/user/**").hasRole("ROLE_USER") // /user/** richiede ruolo USER
            //     .anyRequest().permitAll() // Tutte le altre richieste richiedono autenticazione
            )
            .formLogin(form -> form
                .loginPage("/login") // Pagina di login personalizzata
                .defaultSuccessUrl("/dashboard") // Reindirizzamento dopo login riuscito
                .failureUrl("/login?error=true") // Reindirizzamento dopo login fallito
                .permitAll() // Permetti l'accesso alla pagina di login
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true") // Reindirizzamento dopo logout
                .invalidateHttpSession(true) // Invalida la sessione HTTP
                .permitAll() // Permetti l'accesso alla pagina di logout
            )
            .httpBasic(basic -> basic.disable()); // Disabilita HTTP Basic
        http.headers(headers -> headers.frameOptions().disable());//da utilizzare solo per h2 durante lo svilupo


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //     .withUser("user").password("123").roles("USER")
    //     .and()
    //     .withUser("admin").password("456").roles("ADMIN")
    //     .and().passwordEncoder(NoOpPasswordEncoder.getInstance());
    // }
}
