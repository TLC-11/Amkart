package com.amkart.estore.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailsResponse {
	private int quantity;
	private float totalprice;
	private float discountprice;
	private float discount;
	private List<String> productname;
}
