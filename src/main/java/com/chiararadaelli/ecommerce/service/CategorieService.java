package com.chiararadaelli.ecommerce.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chiararadaelli.ecommerce.model.Categorie;

import com.chiararadaelli.ecommerce.repository.CategorieRepository;

import jakarta.transaction.Transactional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Transactional
    public boolean createCategoria(Categorie categoria) {
        if (categoria.getNomeCategorie() == null || categoria.getNomeCategorie().trim().isEmpty()) {
            return false; // Non salvare se il nome è vuoto o nullo
        }

        if (categorieRepository.findByNomeCategorie(categoria.getNomeCategorie()) != null) {
            // Potresti lanciare un'eccezione specifica qui, ad esempio
            // throw new CategoryAlreadyExistsException("Categoria con nome " + categoria.getNomeCategorie() + " già esistente.");
            System.err.println("Errore: Categoria con nome " + categoria.getNomeCategorie() + " già esistente.");
            return false;
        }

        try {
            Categorie savedCategoria = categorieRepository.save(categoria);
            return savedCategoria.getCategorieId() != null && savedCategoria.getCategorieId() > 0;
        } catch (Exception e) {
            // Log dell'errore
            e.printStackTrace();
            return false;
        }
    }

    public void updateCategoria(Categorie categoriaAggiornata) {
        categorieRepository.save(categoriaAggiornata);
    }
    

    public List<Categorie> getAllCategorie() {
        return categorieRepository.findAll();
    }
    
    public void deleteCategoria(Long id) {
        categorieRepository.deleteById(id);
    }
    
   

    public Categorie getCategoriaById(Long categorieId) {
        return categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata con ID: " + categorieId));
    }
    

    // ... altri metodi per recuperare, aggiornare, eliminare categorie
}