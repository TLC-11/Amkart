package com.amkart.estore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderitemDto {

	private int orderitemid;
	private int quantity;
	private float amount;
	private ProductDto product;

}
