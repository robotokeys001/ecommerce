package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chiararadaelli.ecommerce.model.Prodotti;

public interface ProdottiRepository extends JpaRepository<Prodotti, Long>{
    
}
