package com.chiararadaelli.ecommerce.service;

import com.chiararadaelli.ecommerce.model.Ruoli;
import com.chiararadaelli.ecommerce.repository.RuoliRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RuoliService {

    @Autowired
    private RuoliRepository ruoliRepository;

    @Transactional
    public Ruoli createRuolo(Ruoli ruolo) {
        if (ruolo.getNomeRuolo() == null || ruolo.getNomeRuolo().trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del ruolo non può essere vuoto.");
        }
        if (ruoliRepository.findByNomeRuolo(ruolo.getNomeRuolo()) != null) {
            throw new IllegalStateException("Un ruolo con nome " + ruolo.getNomeRuolo() + " esiste già.");
        }
        return ruoliRepository.save(ruolo);
    }

    public Optional<Ruoli> getRuoloById(Long id) {
        return ruoliRepository.findById(id);
    }

    public Ruoli getRuoloByNome(String nomeRuolo) {
        return ruoliRepository.findByNomeRuolo(nomeRuolo);
    }

    public List<Ruoli> getAllRuoli() {
        return ruoliRepository.findAll();
    }

    @Transactional
    public Ruoli updateRuolo(Long id, Ruoli ruoloAggiornato) {
        Optional<Ruoli> ruoloEsistente = ruoliRepository.findById(id);
        if (ruoloEsistente.isPresent()) {
            Ruoli ruolo = ruoloEsistente.get();
            if (ruoloAggiornato.getNomeRuolo() != null && !ruoloAggiornato.getNomeRuolo().trim().isEmpty()) {
                if (!ruolo.getNomeRuolo().equals(ruoloAggiornato.getNomeRuolo()) &&
                    ruoliRepository.findByNomeRuolo(ruoloAggiornato.getNomeRuolo()) != null) {
                    throw new IllegalStateException("Un ruolo con nome " + ruoloAggiornato.getNomeRuolo() + " esiste già.");
                }
                ruolo.setNomeRuolo(ruoloAggiornato.getNomeRuolo());
            }
            return ruoliRepository.save(ruolo);
        } else {
            // Potresti lanciare un'eccezione specifica (es. RuoloNonTrovatoException)
            return null;
        }
    }

    @Transactional
    public void deleteRuolo(Long id) {
        // Potresti aggiungere qui controlli per assicurarti che nessun utente stia usando questo ruolo
        ruoliRepository.deleteById(id);
    }
}