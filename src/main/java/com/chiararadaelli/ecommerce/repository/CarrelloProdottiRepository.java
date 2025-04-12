package com.chiararadaelli.ecommerce.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiararadaelli.ecommerce.model.Carrello;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;

@Repository
public interface CarrelloProdottiRepository extends JpaRepository<CarrelloProdotti, Long>{
    
    @Query("SELECT cp FROM CarrelloProdotti cp JOIN cp.carrello c JOIN c.utente u WHERE u = :utente")
    List<CarrelloProdotti> findByUtente(@Param("utente") Utenti utente);

    List<CarrelloProdotti> findByCarrello(Carrello carrello);
    void deleteByCarrelloAndProdotti(Carrello carrello, Prodotti prodotto);
    CarrelloProdotti findByCarrelloAndProdotti(Carrello carrello, Prodotti prodotto);

    @Query("SELECT SUM(cp.quantita * p.prezzo) FROM CarrelloProdotti cp JOIN cp.prodotti p WHERE cp.carrello = :carrello")
    BigDecimal getTotaleCarrello(@Param("carrello") Carrello carrello);

    void deleteAllByCarrello(Carrello carrello);

    @Query("SELECT cp FROM CarrelloProdotti cp JOIN FETCH cp.prodotti WHERE cp.carrello = :carrello")
    List<CarrelloProdotti> findByCarrelloWithProdotti(@Param("carrello") Carrello carrello);
}
