package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chiararadaelli.ecommerce.model.Prodotti;

public interface ProdottiRepository extends JpaRepository<Prodotti, Long>{
     Page<Prodotti> findAll(Specification<Prodotti> spec, Pageable pageable);
}
