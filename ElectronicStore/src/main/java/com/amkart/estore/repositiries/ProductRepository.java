package com.amkart.estore.repositiries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amkart.estore.entities.Category;
import com.amkart.estore.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	Optional<Product> findByProductname(String name);

	Page<Product> findByIslive(boolean req, Pageable pageable);

	Page<Product> findByStock(boolean req, Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable pageable);

	@Query(value = "select p from Product p where p.discountprice<?1")
	List<Product> getAllProductsUnderDiscountprice(float discountprice);

	@Query(value = "select * from Products p where p.price<:amt", nativeQuery = true)
	List<Product> getAllProductsUnderPrice(@Param(value = "amt") float amt);

}
