package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chiararadaelli.ecommerce.model.Ruoli;



public interface RuoliRepository extends JpaRepository<Ruoli, Long>{
    
    Ruoli getRuoloByNomeRuolo(String nomeRuolo);
}
