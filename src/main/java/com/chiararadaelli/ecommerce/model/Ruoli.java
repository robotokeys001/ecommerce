package com.chiararadaelli.ecommerce.model;

   
import lombok.Data;

import java.util.List;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "ruoli")
public class Ruoli {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    private Long ruoliId;

    @Column(name = "nomeruolo")
    private String nomeRuolo;

    @OneToMany(mappedBy = "ruoli")
    private List<Utenti> utenti;
}



