package com.chiararadaelli.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name= "cart_items")
public class CarrelloProdotti {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "prodotti_id")
    private Prodotti prodotti;

    @ManyToOne
    @JoinColumn(name = "utenti_id")
    private Utenti utenti;

    private int quantita;
}
