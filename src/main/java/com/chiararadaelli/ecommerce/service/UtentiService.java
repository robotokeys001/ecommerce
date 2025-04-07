package com.chiararadaelli.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chiararadaelli.ecommerce.costanti.Costanti;
import com.chiararadaelli.ecommerce.model.Ruoli;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.RuoliRepository;
import com.chiararadaelli.ecommerce.repository.UtentiRepository;

@Service
public class UtentiService {
    
    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private RuoliRepository ruoliRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Utenti utente){
        boolean isSaved = false;
        Ruoli ruolo = ruoliRepository.getRuoloByNomeRuolo(Costanti.UTENTE_ROLE);
        utente.setRuoli(ruolo);
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente = utentiRepository.save(utente);
        if (null != utente && utente.getUtentiId() > 0)
        {
            isSaved = true;
        }
        return isSaved;
    }

}
