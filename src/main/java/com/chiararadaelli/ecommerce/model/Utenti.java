package com.chiararadaelli.ecommerce.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "utenti")
public class Utenti {
    
    @Id
     @Column(name = "utenti_id")
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    private Long utentiId;

   
    private String nome;

    private String email;

    private String password;


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Ruoli.class)
    @JoinColumn(name = "ruoli_id", referencedColumnName = "ruoliId",nullable = false)
    private Ruoli ruoli;

}
