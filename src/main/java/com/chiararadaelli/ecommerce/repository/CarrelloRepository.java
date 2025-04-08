package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.Utenti;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    
     Carrello findByUtenti(Utenti utenti);

    // Potresti anche considerare un metodo per verificare se un utente ha già un carrello
    boolean existsByUtenti(Utenti utenti);
}
