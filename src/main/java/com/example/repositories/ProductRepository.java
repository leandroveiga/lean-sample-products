package com.example.repositories;

import com.example.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.math.BigDecimal;

/**
 * Created by pedroxs on 11/11/15.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findById(Long id, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    Page<Product> findByPrice(BigDecimal price, Pageable pageable);
    Page<Product> findByAvailable(Integer available, Pageable pageable);
    Page<Product> findByCategories_NameIgnoreCaseOrCategories_ParentCategory_NameIgnoreCase(String name, String parentName, Pageable pageable);

}
