package com.chiararadaelli.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chiararadaelli.ecommerce.model.CarrelloProdotti;
import com.chiararadaelli.ecommerce.model.Utenti;

@Repository
public interface CarrelloProdottiRepository extends JpaRepository<CarrelloProdotti, Long>{
    
    public List<CarrelloProdotti> findByUtenti(Utenti utenti);
}
