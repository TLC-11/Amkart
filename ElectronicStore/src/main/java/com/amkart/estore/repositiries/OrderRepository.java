package com.amkart.estore.repositiries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.Order;
import com.amkart.estore.entities.User;

public interface OrderRepository extends JpaRepository<Order, String> {
	Page<Order> findByUser(User user, Pageable pageable);


}
