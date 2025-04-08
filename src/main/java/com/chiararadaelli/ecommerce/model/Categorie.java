package com.chiararadaelli.ecommerce.model;

import java.util.List;

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

    public Categorie(){
        this.categorieId= categorieId;
        this.nomeCategorie= nomeCategorie;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorieId;

    private String nomeCategorie;

    @OneToMany(mappedBy = "categorie")
    private List<Prodotti> prodotti;

    public Categorie orElse(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElse'");
    }
}
