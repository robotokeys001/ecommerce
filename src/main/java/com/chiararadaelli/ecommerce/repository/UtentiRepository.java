package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chiararadaelli.ecommerce.model.Utenti;



public interface UtentiRepository extends JpaRepository<Utenti, Long>{
    
    Utenti readByEmail(String email);
}
