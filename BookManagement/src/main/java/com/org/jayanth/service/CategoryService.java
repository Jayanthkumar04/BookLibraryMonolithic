package com.org.jayanth.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.org.jayanth.dto.CategoryDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Category;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto category);

    CategoryDto updateCategory(Long id, CategoryDto category);

    MessageDto deleteCategory(Long id);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategories();  // for dropdown

    Page<Category> getCategories(Pageable pageable);  // paginated list
	
}
