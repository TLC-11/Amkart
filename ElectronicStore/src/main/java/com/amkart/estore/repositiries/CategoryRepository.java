package com.amkart.estore.repositiries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

	Category findByName(String name);

	Page<Category> findByNameContains(String infix, Pageable pageable);

}
