package com.chiararadaelli.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chiararadaelli.ecommerce.model.Categorie;
import com.chiararadaelli.ecommerce.model.Prodotti;
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

    public Categorie getCategoriaById(Long categorieId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoriaById'");
    }

    public List<Categorie> getAllCategorie() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCategorie'");
    }

    public void updateCategorie(Long id, Categorie categorie) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategorie'");
    }

    public void updateCategoria(Categorie categoriaAggiornata) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategoria'");
    }

    public List<Categorie> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public Optional<Prodotti> findById(Long categorieId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    // ... altri metodi per recuperare, aggiornare, eliminare categorie
}