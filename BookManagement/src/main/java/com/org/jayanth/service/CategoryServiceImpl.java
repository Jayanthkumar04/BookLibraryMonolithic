package com.org.jayanth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public CategoryDto createCategory(CategoryDto category) {

        logger.info("Create category initiated | name={}", category.getName());

        if (categoryRepo.existsByName(category.getName())) {
            logger.warn("Category already exists | name={}", category.getName());
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        Category c = new Category();
        c.setName(category.getName());
        c.setDescription(category.getDescription());

        categoryRepo.save(c);

        logger.info("Create category successful | categoryId={} name={}", c.getId(), c.getName());

        return category;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto category) {

        logger.info("Update category initiated | categoryId={}", id);

        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        CategoryDto dto = new CategoryDto();
        dto.setId(id);

        if (category.getName() != null) {
            existing.setName(category.getName());
            dto.setName(category.getName());
        } else {
            dto.setName(existing.getName());
        }

        if (category.getDescription() != null) {
            existing.setDescription(category.getDescription());
            dto.setDescription(category.getDescription());
        } else {
            dto.setDescription(existing.getDescription());
        }

        categoryRepo.save(existing);

        logger.info("Update category successful | categoryId={}", id);

        return new CategoryDto(id, category.getName(), category.getDescription());
    }

    @Override
    public MessageDto deleteCategory(Long id) {

        logger.info("Delete category initiated | categoryId={}", id);

        if (getCategoryById(id) == null) {
            throw new CategoryNotFoundException("category doesnot exists");
        }

        categoryRepo.deleteById(id);

        logger.info("Delete category successful | categoryId={}", id);

        return new MessageDto("category has been deleted successfully");
    }

    @Override
    public CategoryDto getCategoryById(Long id) {

        logger.info("Get category by id initiated | categoryId={}", id);

        Category cat = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        logger.info("Get category by id successful | categoryId={}", id);

        return new CategoryDto(id, cat.getName(), cat.getDescription());
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        logger.info("Get all categories initiated");

        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> dtos = new ArrayList<>();

        for (Category c : categories) {
            dtos.add(new CategoryDto(c.getId(), c.getName(), c.getDescription()));
        }

        logger.info("Get all categories successful | count={}", dtos.size());

        return dtos;
    }

    @Override
    public Page<Category> getCategories(Pageable pageable) {

        logger.info("Get paged categories initiated | page={} size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Category> page = categoryRepo.findAll(pageable);

        logger.info("Get paged categories successful | totalElements={}", page.getTotalElements());

        return page;
    }
}
