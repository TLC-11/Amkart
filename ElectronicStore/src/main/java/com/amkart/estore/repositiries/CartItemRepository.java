package com.amkart.estore.repositiries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.Cart_Item;

public interface CartItemRepository extends JpaRepository<Cart_Item, Integer> {

}
