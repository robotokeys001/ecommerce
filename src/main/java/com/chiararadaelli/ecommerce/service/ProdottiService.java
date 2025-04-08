package com.chiararadaelli.ecommerce.service;

import com.chiararadaelli.ecommerce.model.Categorie;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.repository.ProdottiRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProdottiService {

    @Autowired
    private ProdottiRepository prodottiRepository;

    @Autowired
    private CategorieService categorieService; // Per la gestione delle categorie

    @Transactional
    public Prodotti createProdotto(Prodotti prodotto) {
        // Aggiungi qui eventuali logiche di validazione prima del salvataggio
        if (prodotto.getNomeProdotto() == null || prodotto.getNomeProdotto().trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del prodotto non può essere vuoto.");
        }
        if (prodotto.getPrezzo() == null || prodotto.getPrezzo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Il prezzo del prodotto deve essere maggiore di zero.");
        }
        if (prodotto.getCategorie() == null || prodotto.getCategorie().getCategorieId() == null) {
            throw new IllegalArgumentException("Il prodotto deve essere associato a una categoria.");
        }
        // Verifica che la categoria esista
        Categorie categoria = categorieService.getCategoriaById(prodotto.getCategorie().getCategorieId());
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria specificata non esiste.");
        }
        prodotto.setCategorie(categoria);
        return prodottiRepository.save(prodotto);
    }

    public Optional<Prodotti> getProdottoById(Long id) {
        return prodottiRepository.findById(id);
    }

    public Page<Prodotti> getAllProdotti(Pageable pageable) {
        return prodottiRepository.findAll(pageable);
    }

    public Page<Prodotti> searchProdotti(String nome, String brand, Long categoriaId, BigDecimal minPrezzo, BigDecimal maxPrezzo, Pageable pageable) {
        Specification<Prodotti> spec = Specification.where(null);

        if (nome != null && !nome.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeProdotto")), "%" + nome.toLowerCase() + "%"));
        }

        if (brand != null && !brand.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase()));
        }

        if (categoriaId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("categorie").get("categorieId"), categoriaId));
        }

        if (minPrezzo != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("prezzo"), minPrezzo));
        }

        if (maxPrezzo != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("prezzo"), maxPrezzo));
        }

        return prodottiRepository.findAll(spec, pageable);
    }

    @Transactional
    public Prodotti updateProdotto(Long id, Prodotti prodottoAggiornato) {
        Optional<Prodotti> prodottoEsistente = prodottiRepository.findById(id);
        if (prodottoEsistente.isPresent()) {
            Prodotti prodotto = prodottoEsistente.get();
            prodotto.setNomeProdotto(prodottoAggiornato.getNomeProdotto());
            prodotto.setBrand(prodottoAggiornato.getBrand());
            prodotto.setDescrizione(prodottoAggiornato.getDescrizione());
            prodotto.setInventario(prodottoAggiornato.getInventario());
            prodotto.setPrezzo(prodottoAggiornato.getPrezzo());
            prodotto.setImmagine(prodottoAggiornato.getImmagine());
            if (prodottoAggiornato.getCategorie() != null && prodottoAggiornato.getCategorie().getCategorieId() != null) {
                Categorie categoria = categorieService.getCategoriaById(prodottoAggiornato.getCategorie().getCategorieId());
                if (categoria == null) {
                    throw new IllegalArgumentException("La categoria specificata non esiste.");
                }
                prodotto.setCategorie(categoria);
            }
            return prodottiRepository.save(prodotto);
        } else {
            // Potresti lanciare un'eccezione specifica (es. ProdottoNonTrovatoException)
            return null;
        }
    }

    @Transactional
    public void deleteProdotto(Long id) {
        prodottiRepository.deleteById(id);
    }

    @Transactional
    public void aggiornaInventario(Long prodottoId, int quantita) {
        Optional<Prodotti> prodottoEsistente = prodottiRepository.findById(prodottoId);
        if (prodottoEsistente.isPresent()) {
            Prodotti prodotto = prodottoEsistente.get();
            prodotto.setInventario(prodotto.getInventario() + quantita);
            prodottiRepository.save(prodotto);
        } else {
            log.warn("Tentativo di aggiornare l'inventario di un prodotto inesistente con ID: {}", prodottoId);
            // Potresti decidere se lanciare un'eccezione qui
        }
    }

    /*-------------gestione immagini---------------------------------- */
private final Path imageUploadPath = Paths.get("uploads"); // Dovresti configurare questo esternamente
    @Transactional
public Prodotti createProdottoWithImage(
        String nomeProdotto, Long categorieId, BigDecimal prezzo, int quantita,
        String descrizzione, MultipartFile immagine) {

    try {
        if (!Files.exists(imageUploadPath)) {
            Files.createDirectories(imageUploadPath);
        }

        String filename = immagine.getOriginalFilename();
        Path filePath = imageUploadPath.resolve(filename);
        Files.copy(immagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Categorie categoria = categorieService.getCategoriaById(categorieId);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria non trovata con ID: " + categorieId);
        }

        Prodotti prodotto = new Prodotti();
        prodotto.setNomeProdotto(nomeProdotto);
        prodotto.setCategorie(categoria);
        prodotto.setDescrizione(descrizzione);
        prodotto.setPrezzo(prezzo);
        prodotto.setImmagine(filename);
        prodotto.setInventario(quantita);

        return prodottiRepository.save(prodotto);

    } catch (Exception e) {
        log.error("Errore durante la creazione del prodotto con immagine", e);
        throw new RuntimeException("Errore durante la creazione del prodotto.", e); // Rilancia un'eccezione gestita dal controller
        }
    }

    public List<Prodotti> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    
}