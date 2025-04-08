package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiararadaelli.ecommerce.model.Categorie;

@Repository
public interface CategorieRepository  extends JpaRepository<Categorie, Long>{

    Categorie getCategoriaByNome(String nomeCategorie);

    Object findByNomeCategorie(String nomeCategorie);
    
} 