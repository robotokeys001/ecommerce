package com.chiararadaelli.ecommerce.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.data.jpa.domain.Specification;
import com.chiararadaelli.ecommerce.model.Categorie;
import com.chiararadaelli.ecommerce.model.Prodotti;
import com.chiararadaelli.ecommerce.model.Utenti;
import com.chiararadaelli.ecommerce.repository.CategorieRepository;
import com.chiararadaelli.ecommerce.repository.ProdottiRepository;
import com.chiararadaelli.ecommerce.repository.UtentiRepository;
import com.chiararadaelli.ecommerce.service.CategorieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private ProdottiRepository prodottiRepository;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private CategorieRepository categorieRepository;

    private final Path imageUploadPath = Paths.get("uploads");

    @GetMapping("/categorie")
    public ModelAndView getcategory() {
        ModelAndView mView = new ModelAndView("categories");
        List<Categorie> categoria = categorieRepository.findAll();
        mView.addObject("categoria", categoria);
        return mView;
    }

    @PostMapping("/categorie")
    public String addCategoria(@RequestParam("nomecategoria") String nome, RedirectAttributes redirectAttributes) {
        try {
            Categorie categoriaEsistente = categorieRepository.getCategoriaByNome(nome);
            if (categoriaEsistente != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Categoria già esistente.");
                return "redirect:categorie";
            }
            Categorie nuovaCategoria = new Categorie();
            nuovaCategoria.setNome(nome);
            categorieRepository.save(nuovaCategoria);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiunta con successo.");
        } catch (Exception e) {
            log.error("Errore durante l'aggiunta della categoria", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiunta della categoria.");
        }
        return "redirect:categorie";
    }

    @GetMapping("categorie/update")
    public String updateCategory(@RequestParam("categoryid") Long id, @RequestParam("nome") String nome, RedirectAttributes redirectAttributes) {
        try{
            Categorie categoria = categorieRepository.findById(id).orElse(null);
            if (categoria != null) {
                categoria.setNome(nome);
                categorieRepository.save(categoria);
                redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiornata con successo.");
            }
        }catch(Exception e){
            log.error("Errore durante l'aggiornamento della categoria", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiornamento della categoria.");
        }
        return "redirect:/admin/categorie";
    }

    @GetMapping("products/add")
    public ModelAndView addProduct() {
        ModelAndView mView = new ModelAndView("productsAdd");
        List<Categorie> categories = categorieRepository.findAll();
        mView.addObject("categories", categories);
        return mView;
    }

    @GetMapping("products/update/{id}")
    public ModelAndView updateproduct(@PathVariable("id") Long id) {
        ModelAndView mView = new ModelAndView("productsUpdate");
        Prodotti prodotto = prodottiRepository.findById(id).orElse(null);
        List<Categorie> categorie = categorieRepository.findAll();
        mView.addObject("categories", categorie);
        mView.addObject("product", prodotto);
        return mView;
    }

    @RequestMapping(value = "products/update/{id}", method = RequestMethod.POST)
    public String updateProduct(@PathVariable("id") Long id, @RequestParam("nome") String nomeProdotto, @RequestParam("categoriaid") Long categorieId, @RequestParam("price") BigDecimal prezzo, @RequestParam("quantita") int quantita, @RequestParam("descrizione") String descrizzione, @RequestParam("immagine") String immagini, RedirectAttributes redirectAttributes) {
        try{
            Prodotti prodotto = prodottiRepository.findById(id).orElse(null);
            if (prodotto != null) {
                Categorie categoria = categorieRepository.findById(categorieId).orElse(null);
                prodotto.setNomeProdotto(nomeProdotto);
                prodotto.setCategorie(categoria);
                prodotto.setDescrizzione(descrizzione);
                prodotto.setPrezzo(prezzo);
                prodotto.setImmagine(immagini);
                prodotto.setInventario(quantita);
                prodottiRepository.save(prodotto);
                redirectAttributes.addFlashAttribute("successMessage", "Prodotto aggiornato con successo.");
            }
        }catch (Exception e){
            log.error("Errore durante l'aggiornamento del prodotto", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiornamento del prodotto.");
        }
        return "redirect:/admin/prodotti";
    }

    @GetMapping("products/delete")
    public String removeProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            prodottiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Prodotto eliminato con successo.");
        }catch (Exception e){
            log.error("Errore durante l'eliminazione del prodotto", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del prodotto.");
        }
        return "redirect:/admin/prodotti";
    }

    @GetMapping("customers")
    public ModelAndView getCustomerDetail() {
        ModelAndView mView = new ModelAndView("displayCustomers");
        List<Utenti> utenti = utentiRepository.findAll();
        mView.addObject("customers", utenti);
        return mView;
    }

    @GetMapping("/prodotti")
    public ModelAndView getProducts(
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {

        ModelAndView mView = new ModelAndView("prodotti");

        Specification<Prodotti> spec = (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likeSearch = "%" + search + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("nomeProdotto"), likeSearch),
                    criteriaBuilder.like(root.get("descrizzione"), likeSearch)
            );
        };

        Page<Prodotti> prodotti = prodottiRepository.findAll(spec, pageable);
        mView.addObject("products", prodotti);
        mView.addObject("search", search);
        return mView;
    }

    @GetMapping("/customers")
    public ModelAndView getCustomers(@PageableDefault(size = 10) Pageable pageable) {
        ModelAndView mView = new ModelAndView("displayCustomers");
        Page<Utenti> utenti = utentiRepository.findAll(pageable);
        mView.addObject("customers", utenti);
        return mView;
    }

    @PostMapping("/products/add")
    public String addProduct(
            @RequestParam("nome") String nomeProdotto,
            @RequestParam("categoriaid") Long categorieId,
            @RequestParam("price") BigDecimal prezzo,
            @RequestParam("quantita") int quantita,
            @RequestParam("descrizione") String descrizzione,
            @RequestParam("immagine") MultipartFile immagine,
            RedirectAttributes redirectAttributes) {

        try {
            if (!Files.exists(imageUploadPath)) {
                Files.createDirectories(imageUploadPath);
            }

            String filename = immagine.getOriginalFilename();
            Path filePath = imageUploadPath.resolve(filename);
            Files.copy(immagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Categorie categoria = categorieRepository.findById(categorieId).orElse(null);
            Prodotti prodotto = new Prodotti();
            prodotto.setNomeProdotto(nomeProdotto);
            prodotto.setCategorie(categoria);
            prodotto.setDescrizzione(descrizzione);
            prodotto.setPrezzo(prezzo);
            prodotto.setImmagine(filename);
            prodotto.setInventario(quantita);
            prodottiRepository.save(prodotto);

            redirectAttributes.addFlashAttribute("successMessage", "Prodotto aggiunto con successo.");
        } catch (Exception e) {
            log.error("Errore durante l'aggiunta del prodotto", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'aggiunta del prodotto.");
        }

        return "redirect:/admin/prodotti";
    }
}