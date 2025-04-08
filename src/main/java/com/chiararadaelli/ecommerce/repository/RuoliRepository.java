package com.chiararadaelli.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chiararadaelli.ecommerce.model.Ruoli;

@Repository
public interface RuoliRepository extends JpaRepository<Ruoli, Long>{
    
   Ruoli findByNomeRuolo(String nomeRuolo);

}
