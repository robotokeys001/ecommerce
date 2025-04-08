package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chiararadaelli.ecommerce.model.Utenti;

@Repository
public interface UtentiRepository extends JpaRepository<Utenti, Long>{
    
    Utenti findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByEmail(String email);
    Utenti findByEmail(String email); // Convenzione più comune per la ricerca
}
