package com.amkart.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amkart.estore.dtos.OrderDetailsRequest;
import com.amkart.estore.dtos.OrderDto;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.UserResponse;
import com.amkart.estore.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("e/iot/amkart")
@Tag(name="Order module", description = "Operation related to orders")
public class OrderController {
	@Autowired
	OrderService service;

	@PostMapping("/placeorder")
	ResponseEntity<OrderDto> placeOrderRequest(@RequestBody OrderDetailsRequest orderdetails) {
		OrderDto placeOrder = service.placeOrder(orderdetails);
		return ResponseEntity.status(HttpStatus.CREATED).body(placeOrder);

	}

	@PutMapping("/updateorderdetails/orderid/{orderid}")
	ResponseEntity<OrderDto> updateItemRequest(@PathVariable String orderid, @RequestBody OrderDto orderdto) {
		OrderDto updateOrderDetails = service.updateOrderDetails(orderdto, orderid);
		return ResponseEntity.status(HttpStatus.CREATED).body(updateOrderDetails);

	}

	@DeleteMapping("/removeorderitem/itemid/{itemid}")
	ResponseEntity<UserResponse> removeOrderItemRequest(@PathVariable int itemid) {
		service.cancelItem(itemid);
		UserResponse resp = UserResponse.builder().message("item cancelled").status("success").build();
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@DeleteMapping("/cancelorder/orderid/{orderid}")
	ResponseEntity<UserResponse> cancelOrder(@PathVariable String orderid) {
		service.cancelOrder(orderid);
		UserResponse resp = UserResponse.builder().message("order cancelled").status("success").build();
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@GetMapping("/orderdetails/userid/{userid}")
	ResponseEntity<PageDetailsResponse<OrderDto>> getOrderDetails(@PathVariable String userid,
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "5") int pagesize,
			@RequestParam(required = false, defaultValue = "orderdate") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<OrderDto> resp = service.getOrderDetailsByUser(pagenumber, pagesize, sortby, sortdir,
				userid);
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}

	@GetMapping("/allorderdetails")
	ResponseEntity<PageDetailsResponse<OrderDto>> getallOrderDetails(
			@RequestParam(required = false, defaultValue = "0") int pagenumber,
			@RequestParam(required = false, defaultValue = "5") int pagesize,
			@RequestParam(required = false, defaultValue = "orderdate") String sortby,
			@RequestParam(required = false, defaultValue = "asc") String sortdir) {
		PageDetailsResponse<OrderDto> resp = service.getAllOrderDetails(pagenumber, pagesize, sortby, sortdir);
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
}
