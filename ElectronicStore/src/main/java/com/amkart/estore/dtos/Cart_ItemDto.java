package com.amkart.estore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart_ItemDto {

	private int itemid;
	private int quantity;
	private float actualamount;
	private float discountamount;
	private float discount;

	private ProductDto product;

}
