package com.amkart.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amkart.estore.dtos.CartDetailsResponse;
import com.amkart.estore.dtos.CartDto;
import com.amkart.estore.dtos.UserResponse;
import com.amkart.estore.services.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("e/iot/amkart")
@Tag(name="Cart module", description = "Operation related to carts")
public class CartController {
	@Autowired
	CartService service;

	@PostMapping("/additem/userid/{userid}/productid/{productid}/quantity/{quantity}")
	ResponseEntity<CartDto> addItemRequest(@PathVariable String userid, @PathVariable String productid,
			@PathVariable int quantity) {
		CartDto cartdto = service.addItemTocart(userid, productid, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).body(cartdto);

	}

	@PutMapping("/additem/userid/{userid}/productid/{productid}/quantity/{quantity}")
	ResponseEntity<CartDto> updateItemRequest(@PathVariable String userid, @PathVariable String productid,
			@PathVariable int quantity) {
		CartDto cartdto = service.addItemTocart(userid, productid, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).body(cartdto);

	}

	@DeleteMapping("/removeitem/itemid/{itemid}")
	ResponseEntity<UserResponse> removeItemRequest(@PathVariable int itemid) {
		service.removeItem(itemid);
		UserResponse resp = UserResponse.builder().message("deleted").status("success").build();
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@DeleteMapping("/clearcart/userid/{userid}")
	ResponseEntity<UserResponse> clearCart(@PathVariable String userid) {
		service.clearCart(userid);
		UserResponse resp = UserResponse.builder().message("cart is cleared").status("success").build();
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@GetMapping("/cartdetails/userid/{userid}")
	ResponseEntity<CartDetailsResponse> getCartDetails(@PathVariable String userid) {
		CartDetailsResponse details = service.getDetails(userid);
		return ResponseEntity.status(HttpStatus.OK).body(details);
	}

}
