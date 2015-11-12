package com.example.controllers;

import com.example.dto.LocalQuery;
import com.example.entities.Product;
import com.example.repositories.CategoryRepository;
import com.example.repositories.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.example.util.Utils.getSortString;
import static com.example.util.Utils.normalizeQuery;

/**
 * Created by pedroxs on 11/11/15.
 */
@Controller
@RequestMapping("/products")
@SessionAttributes("query")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute("fields")
    public List<String> fields() {
        return Arrays.asList("id", "name", "description", "price", "available", "category");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<Product> products;
        if (model.containsAttribute("query")) {
            products = resolveQuery((LocalQuery) model.asMap().get("query"), pageable, productRepository);
        } else {
            model.addAttribute("query", new LocalQuery());
            products = productRepository.findAll(pageable);
        }
        model.addAttribute("page", products);
        model.addAttribute("sortString", getSortString(products.getSort()));
        return "product/products";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@PageableDefault(size = 5) Pageable pageable, Model model, LocalQuery query) {
        Page<Product> products = resolveQuery(query, pageable, productRepository);
        model.addAttribute("query", query);
        model.addAttribute("page", products);
        model.addAttribute("sortString", getSortString(products.getSort()));
        return "product/products";
    }

    private Page<Product> resolveQuery(LocalQuery query, Pageable pageable, ProductRepository productRepository) {
        switch (normalizeQuery(query)) {
            case "id":
                return productRepository.findById(Long.valueOf(query.getQueryString()), pageable);
            case "name":
                return productRepository.findByNameContainingIgnoreCase(query.getQueryString(), pageable);
            case "description":
                return productRepository.findByDescriptionContainingIgnoreCase(query.getQueryString(), pageable);
            case "price":
                return productRepository.findByPrice(new BigDecimal(query.getQueryString()), pageable);
            case "available":
                return productRepository.findByAvailable(Integer.valueOf(query.getQueryString()), pageable);
            case "category":
                return productRepository.findByCategories_NameIgnoreCaseOrCategories_ParentCategory_NameIgnoreCase(query.getQueryString(), query.getQueryString(), pageable);
            default:
                return productRepository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") String id, @RequestParam(value = "edit", defaultValue = "false") Boolean edit, Model model) {
        model.addAttribute("cats", categoryRepository.findAll());
        if ("new".equalsIgnoreCase(id)) {
            model.addAttribute("product", new Product());
            return "product/product";
        }
        Product product = productRepository.findOne(Long.valueOf(id));
        model.addAttribute("product", product);
        return edit ? "product/product" : "product/product-detail";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Product product, Model model) {
        Product save = productRepository.save(product);
        model.addAttribute("product", save);
        return "product/product-detail";
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
    public String edit(@PathVariable("productId") String id, @Valid Product product, Model model) {
        if (!Long.valueOf(id).equals(product.getId())) {
            throw new RuntimeException("Ids don't match!");
        }
        Product save = productRepository.save(product);
        model.addAttribute("product", save);
        return "product/product-detail";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productRepository.delete(id);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/products";
    }

}
