package com.amkart.estore.repositiries;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.Cart;
import com.amkart.estore.entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {

	Optional<Cart> findByUser(User user);
}
