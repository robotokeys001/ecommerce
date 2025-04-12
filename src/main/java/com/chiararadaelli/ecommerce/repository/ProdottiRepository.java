package com.chiararadaelli.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiararadaelli.ecommerce.model.Prodotti;

@Repository
public interface ProdottiRepository extends JpaRepository<Prodotti, Long>{
     Page<Prodotti> findAll(Specification<Prodotti> spec, Pageable pageable);
     List<Prodotti> findByBrand(String brand);
     Page<Prodotti> findByCategorieNomeCategorie(String nomeCategorie, Pageable pageable);
     List<Prodotti> findTop5ByOrderByNomeProdottoAsc(); // O Desc
}
