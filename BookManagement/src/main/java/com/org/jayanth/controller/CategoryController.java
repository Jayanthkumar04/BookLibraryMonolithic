package com.org.jayanth.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.CategoryDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Category;
import com.org.jayanth.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	// CREATE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDto category) {
        CategoryDto saved = categoryService.createCategory(category);
        return ResponseEntity.created(
                URI.create("/api/categories/" + saved.getName())
        ).body(saved);
    }
    
 // UPDATE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto category) {
    
    	return ResponseEntity.ok(categoryService.updateCategory(id, category));
   
    }
	
 // DELETE (Admin Only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public MessageDto delete(@PathVariable Long id) {
        
    	return categoryService.deleteCategory(id);
        
    }
    
    // GET BY ID (Public)
    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    
 // GET ALL (Public)
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.getAllCategories();
    }
	
}
