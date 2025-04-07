package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chiararadaelli.ecommerce.model.Categorie;

public interface CategorieRepository  extends JpaRepository<Categorie, Long>{

    Categorie getCategoriaByNome(String nome);
    
} 