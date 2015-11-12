package com.example.controllers;

import com.example.dto.LocalQuery;
import com.example.entities.Category;
import com.example.entities.Product;
import com.example.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.example.util.Utils.getSortString;
import static com.example.util.Utils.normalizeQuery;

/**
 * Created by pedroxs on 12/11/15.
 */
@Controller
@RequestMapping("/categories")
@SessionAttributes("query")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute("fields")
    public List<String> fields() {
        return Arrays.asList("id", "name", "parent");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<Category> categories;
        if (model.containsAttribute("query")) {
            categories = resolveQuery((LocalQuery) model.asMap().get("query"), pageable, categoryRepository);
        } else {
            categories = categoryRepository.findAll(pageable);
            model.addAttribute("query", new LocalQuery());
        }
        model.addAttribute("page", categories);
        model.addAttribute("sortString", getSortString(categories.getSort()));
        return "category/categories";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@PageableDefault(size = 5) Pageable pageable, Model model, LocalQuery query) {
        Page<Category> categories = resolveQuery(query, pageable, categoryRepository);
        model.addAttribute("query", query);
        model.addAttribute("page", categories);
        model.addAttribute("sortString", getSortString(categories.getSort()));
        return "category/categories";
    }

    private Page<Category> resolveQuery(LocalQuery query, Pageable pageable, CategoryRepository categoryRepository) {
        switch (normalizeQuery(query)) {
            case "id":
                return categoryRepository.findById(Long.valueOf(query.getQueryString()), pageable);
            case "name":
                return categoryRepository.findByNameContainingIgnoreCase(query.getQueryString(), pageable);
            case "parent":
                return categoryRepository.findByParentCategoryNameContainingIgnoreCase(query.getQueryString(), pageable);
            default:
                return categoryRepository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") String id, @RequestParam(value = "edit", defaultValue = "false") Boolean edit, Model model) {
        model.addAttribute("cats", categoryRepository.findAll());
        if ("new".equalsIgnoreCase(id)) {
            model.addAttribute("category", new Category());
            return "category/category";
        }
        Category category = categoryRepository.findOne(Long.valueOf(id));
        model.addAttribute("category", category);
        return edit ? "category/category" : "category/category-detail";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Category category, Model model) {
        Category save = categoryRepository.save(category);
        model.addAttribute("category", save);
        return "category/category-detail";
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
    public String edit(@PathVariable("categoryId") String id, @Valid Category category, Model model) {
        if (!Long.valueOf(id).equals(category.getId())) {
            throw new RuntimeException("Ids don't match!");
        }
        Category save = categoryRepository.save(category);
        model.addAttribute("category", save);
        return "category/category-detail";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        categoryRepository.delete(id);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/categories";
    }
}
