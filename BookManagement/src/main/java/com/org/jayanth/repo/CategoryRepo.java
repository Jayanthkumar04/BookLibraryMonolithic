package com.org.jayanth.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.jayanth.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Long>{

	boolean existsByName(String name);
}
