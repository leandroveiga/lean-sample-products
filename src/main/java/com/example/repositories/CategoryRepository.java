package com.example.repositories;

import com.example.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pedroxs on 11/11/15.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findById(Long id, Pageable pageable);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Category> findByParentCategoryNameContainingIgnoreCase(String name, Pageable pageable);
}
