package com.chiararadaelli.ecommerce.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "ruoli")
public class Ruoli {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long ruoliId;

    @Column(name = "nomeruolo")
    private String nomeRuolo;

    // Rimuovi o commenta la relazione con Utenti nel toString()
    // @OneToMany(mappedBy = "ruoli")
    // private List<Utenti> utenti;

    @Override
    public String toString() {
        return "Ruoli{" +
               "ruoliId=" + ruoliId +
               ", nomeRuolo='" + nomeRuolo + '\'' +
               '}';
    }
}