package com.chiararadaelli.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.chiararadaelli.ecommerce.model.Categorie;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.service.CategorieService;
import com.chiararadaelli.ecommerce.service.ProdottiService;
import com.chiararadaelli.ecommerce.service.UtentiService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final UtentiService utentiService;

    @Autowired
    public AdminController(UtentiService utentiService) {
        this.utentiService = utentiService;
    }
    @Autowired
    private ProdottiService prodottiService;
    @Autowired
    private CategorieService categorieService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "adminDashboard"; // nome della view (es: adminDashboard.html)
    }


/*------------------------gestione categorie-------------------------- */
    @GetMapping("/categorie")
    public ModelAndView getcategoria() {
        ModelAndView mView = new ModelAndView("categorie");
        List<Categorie> categoria = categorieService.getAllCategorie();
        mView.addObject("categoria", categoria);
        return mView;
    }

    @PostMapping("/categorie")
public String addCategoria(@RequestParam("nomecategoria") String nome, RedirectAttributes redirectAttributes) {
    try {
        Categorie nuovaCategoria = new Categorie();
        nuovaCategoria.setNomeCategorie(nome);
        categorieService.createCategoria(nuovaCategoria);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiunta con successo.");
    } catch (Exception e) {
        log.error("Errore durante l'aggiunta della categoria", e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
     return "redirect:/admin/categorie";
    
}

@GetMapping("/categorie/update")
public String updateCategory(@RequestParam("categoryid") Long id, @RequestParam("nome") String nome, RedirectAttributes redirectAttributes) {
    try {
        Categorie categoriaAggiornata = new Categorie();
        categoriaAggiornata.setCategorieId(id);
        categoriaAggiornata.setNomeCategorie(nome);
        categorieService.updateCategoria(categoriaAggiornata); // Assumendo un metodo update in CategorieService
        redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiornata con successo.");
    } catch (Exception e) {
        log.error("Errore durante l'aggiornamento della categoria", e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return "redirect:/admin/categorie";
}

@GetMapping("/categorie/delete")
public String deleteCategoria(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
    try {
        categorieService.deleteCategoria(id);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria eliminata con successo.");
    } catch (Exception e) {
        log.error("Errore durante l'eliminazione della categoria", e);
        redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione della categoria.");
    }
    return "redirect:/admin/categorie";
}


/*-----------------gestione prodotti-------------------------------------- */

    @GetMapping("prodotti/add")
    public ModelAndView addProduct() {
        ModelAndView mView = new ModelAndView("productsAdd");
        List<Categorie> categorie = categorieService.getAllCategorie();
        mView.addObject("categorie", categorie);
        return mView;
    }

    @GetMapping("prodotti/update/{id}")
    public ModelAndView updateprodotti(@PathVariable("id") Long id) {
        ModelAndView mView = new ModelAndView("productsUpdate");
        Prodotti prodotto = prodottiService.getProdottoById(id).orElse(null);
        List<Categorie> categorie = categorieService.getAllCategorie();
        mView.addObject("categorie", categorie);
        mView.addObject("prodotti", prodotto);
        return mView;
    }

    @RequestMapping(value = "prodotti/update/{id}", method = RequestMethod.POST)
public String updateProduct(@PathVariable("id") Long id, @RequestParam("nome") String nomeProdotto, @RequestParam("categoriaid") Long categorieId,@RequestParam("brand") String brand, @RequestParam("price") BigDecimal prezzo, @RequestParam("inventario") int inventario, @RequestParam("descrizione") String descrizione, @RequestParam("immagine") String immagini, RedirectAttributes redirectAttributes) {
    try{
        Prodotti prodotto = prodottiService.getProdottoById(id).orElse(null);
        if (prodotto != null) {
            Categorie categoria = categorieService.getCategoriaById(categorieId);
            prodotto.setNomeProdotto(nomeProdotto);
            prodotto.setCategorie(categoria);
            prodotto.setBrand(brand);
            prodotto.setDescrizione(descrizione);
            prodotto.setPrezzo(prezzo);
            prodotto.setImmagine(immagini);
            prodotto.setInventario(inventario);
            prodottiService.updateProdotto(id, prodotto); // Passa l'ID del prodotto
            redirectAttributes.addFlashAttribute("successMessage", "Prodotto aggiornato con successo.");
        }
    }catch (Exception e){
        log.error("Errore durante l'aggiornamento del prodotto", e);
        redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiornamento del prodotto.");
    }
    return "redirect:/admin/prodotti";
}

    @GetMapping("prodotti/delete")
    public String removeProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            prodottiService.deleteProdotto(id);
            redirectAttributes.addFlashAttribute("successMessage", "Prodotto eliminato con successo.");
        }catch (Exception e){
            log.error("Errore durante l'eliminazione del prodotto", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del prodotto.");
        }
        return "redirect:/admin/prodotti";
    }


    /*-----------------------gestione utenti------------------------------------ */
    @GetMapping("utenti")
    public ModelAndView getCustomerDetail() {
        ModelAndView mView = new ModelAndView("displayUtenti");
        List<Utenti> utenti = utentiService.getAllUtenti();
        mView.addObject("utenti", utenti);
        return mView;
    }
/*-----------------------ricerca prodotti--------------------------------------- */
@GetMapping("/prodotti")
public ModelAndView getProducts(
        @RequestParam(value = "search", required = false) String search,
        @PageableDefault(size = 10) Pageable pageable) {

    ModelAndView mView = new ModelAndView("prodotti");
        Page<Prodotti> prodotti = prodottiService.searchProdotti(search, null, null, null, null, pageable); // Adatta i parametri se necessario
        mView.addObject("prodotti", prodotti);
        mView.addObject("search", search);
        return mView;
    }
/*-------------------------crea prodotti------------------------------------- */
  @PostMapping("/prodotti/add")
    public String addProduct(
            @RequestParam("nome") String nomeProdotto,
            @RequestParam("brand") String brand,
            @RequestParam("categoriaid") Long categorieId,
            @RequestParam("price") BigDecimal prezzo,
            @RequestParam("inventario") int inventario,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("immagine") MultipartFile immagine,
            RedirectAttributes redirectAttributes) {

        try {
            // Crea la directory di upload se non esiste
            Path imageUploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(imageUploadPath)) {
                Files.createDirectories(imageUploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + immagine.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.copy(immagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            prodottiService.createProdotto(
                    nomeProdotto, categorieId,brand, prezzo, inventario, descrizione, fileName // Salva solo il nome nel DB
            );

            redirectAttributes.addFlashAttribute("successMessage", "Prodotto aggiunto con successo.");

        } catch (IOException e) {
            log.error("Errore durante l'aggiunta del prodotto", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiunta del prodotto.");
        }

        return "redirect:/admin/prodotti";
    }
}