package com.org.jayanth.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.CategoryDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // CREATE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto category) {
        logger.info("Category creation started");

        CategoryDto saved = categoryService.createCategory(category);

        logger.info("Category created successfully with name: {}", saved.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // UPDATE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto category) {
        logger.info("Category updating started for id: {}", id);

        CategoryDto dto = categoryService.updateCategory(id, category);

        logger.info("Category updated successfully for id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // DELETE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long id) {
        logger.info("Category deletion started for id: {}", id);

        MessageDto message = categoryService.deleteCategory(id);

        logger.info("Category deleted successfully for id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // GET BY ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        logger.info("Fetching category by id: {}", id);

        CategoryDto dto = categoryService.getCategoryById(id);

        logger.info("Category fetched successfully for id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // GET ALL (Public)
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        logger.info("Fetching all categories");

        List<CategoryDto> dtos = categoryService.getAllCategories();

        logger.info("Fetched {} categories successfully", dtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
