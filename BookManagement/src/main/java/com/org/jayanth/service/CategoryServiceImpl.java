package com.org.jayanth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.CategoryDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Category;
import com.org.jayanth.exceptions.CategoryAlreadyExistsException;
import com.org.jayanth.exceptions.CategoryNotFoundException;
import com.org.jayanth.repo.CategoryRepo;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public CategoryDto createCategory(CategoryDto category) {
	
		 if (categoryRepo.existsByName(category.getName())) {
	            throw new CategoryAlreadyExistsException("Category already exists");
	        }
		 
		 
		 Category c  = new Category();
		 c.setName(category.getName());
		 c.setDescription(category.getDescription());
		 
	       categoryRepo.save(c);
	
	       return category;
	}

	@Override
	public CategoryDto updateCategory(Long id, CategoryDto category) {
	
		 Category existing = categoryRepo.findById(id)
	                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

		 CategoryDto dto = new CategoryDto();
		 dto.setId(id);
		 if(category.getName() != null) 
		 {
			 existing.setName(category.getName());
	         dto.setName(category.getName());
		 }
		 else {
			 dto.setName(existing.getName());
		 }
			 if(category.getDescription()!=null)  
			 {
				 existing.setDescription(category.getDescription());
                  dto.setDescription(category.getDescription());
			 }
			 else {
				 dto.setDescription(existing.getDescription());
			 }
	     categoryRepo.save(existing);
	
	     
	     
	     return new CategoryDto(id, category.getName(), category.getDescription());
	}

	@Override
	public MessageDto deleteCategory(Long id) {
		
		if(getCategoryById(id) == null) throw new CategoryNotFoundException("category doesnot exists");
		
		categoryRepo.deleteById(id);

		return new MessageDto("category has been deleted successfully");
	}

	@Override
	public CategoryDto getCategoryById(Long id) {
		
		Category cat =  categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    
		return new CategoryDto(id, cat.getName(), cat.getDescription());
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		
		 List<Category> categories =  categoryRepo.findAll();
	
		 List<CategoryDto> dtos = new ArrayList<>();
		 for(Category c : categories)
		 {
			 CategoryDto dto = new CategoryDto(c.getId(),c.getName(),c.getDescription());
			 
			 dtos.add(dto);
		 }
		 
		 return dtos;
		 
	
	}

	@Override
	public Page<Category> getCategories(Pageable pageable) {
	
		  return categoryRepo.findAll(pageable);

	}

	
	
}
