package com.chiararadaelli.ecommerce.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "categorie")
public class Categorie {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categorie_id")
    private Long categorieId;

    @Column(name = "nome_categorie")
    private String nomeCategorie;

    @OneToMany(mappedBy = "categorie")
    private List<Prodotti> prodotti;

    
}
