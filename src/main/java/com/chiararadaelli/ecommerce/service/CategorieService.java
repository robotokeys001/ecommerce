package com.chiararadaelli.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chiararadaelli.ecommerce.model.Categorie;
import com.chiararadaelli.ecommerce.repository.CategorieRepository;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    public boolean createCategoria(Categorie categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            return false; // Non salvare se il nome è vuoto o nullo
        }

        try {
            categoria = categorieRepository.save(categoria);
            return categoria != null && categoria.getCategorieId() > 0;
        } catch (Exception e) {
            // Gestisci l'eccezione (log, ecc.)
            e.printStackTrace(); // Per debug, rimuovi in produzione
            return false;
        }
    }
}