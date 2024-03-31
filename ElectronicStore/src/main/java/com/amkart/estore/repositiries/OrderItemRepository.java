package com.amkart.estore.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.Orderitem;

public interface OrderItemRepository extends JpaRepository<Orderitem, Integer> {

}
