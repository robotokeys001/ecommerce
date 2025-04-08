package com.chiararadaelli.ecommerce.repository;

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
    
    @Query("SELECT cp FROM CarrelloProdotti cp JOIN cp.carrello c JOIN c.utenti u WHERE u = :utente")
    List<CarrelloProdotti> findByUtente(@Param("utente") Utenti utente);

    List<CarrelloProdotti> findByCarrello(Carrello carrello);

    CarrelloProdotti findByCarrelloAndProdotti(Carrello carrello, Prodotti prodotto);

}
