package com.chiararadaelli.ecommerce.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "prodotti")
public class Prodotti {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome")
    private String nomeProdotto;

    private String brand;

    private String descrizzione;

    private int inventario;

    private BigDecimal prezzo;

    private String immagine;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Categorie.class)
    @JoinColumn(name = "categorie_id", referencedColumnName = "categorieId",nullable = false)
    private Categorie categorie;
}
