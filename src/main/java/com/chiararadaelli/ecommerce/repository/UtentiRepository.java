package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;



public interface UtentiRepository extends JpaRepository<Utenti, Long>{
    
    Page<Prodotti> findAll(Specification<Prodotti> spec, Pageable pageable);
    Utenti readByEmail(String email);
}
