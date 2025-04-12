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
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "prodotti")
@ToString(exclude = "categorie") // Esclude l'oggetto 'categorie' dal toString()
@EqualsAndHashCode(exclude = "categorie")
public class Prodotti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome")
    private String nomeProdotto;

    private String brand;

    private String descrizione;

    private int inventario;

    private BigDecimal prezzo;

    private String immagine;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Categorie.class)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    // Alternativa per toString (mostra solo l'ID della categoria):
    // @Override
    // public String toString() {
    //     return "Prodotti{" +
    //            "id=" + id +
    //            ", nomeProdotto='" + nomeProdotto + '\'' +
    //            ", brand='" + brand + '\'' +
    //            ", descrizione='" + descrizione + '\'' +
    //            ", inventario=" + inventario +
    //            ", prezzo=" + prezzo +
    //            ", immagine='" + immagine + '\'' +
    //            ", categorieId=" + (categorie != null ? categorie.getCategorieId() : null) +
    //            '}';
    // }
}