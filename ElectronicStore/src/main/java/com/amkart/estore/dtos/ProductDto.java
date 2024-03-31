package com.amkart.estore.dtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDto {

	private String productid;
	@NotBlank(message = "can't be blank")
	private String productname;
	private String productdesc;
	private String productimg;
	@NotNull(message = "can't be null")
	private float price;
	private float discountprice;
	@NotNull(message = "can't be null")
	private int qunatity;
	private Date addeddate;
	@NotNull(message = "can't be null")
	private boolean islive;
	@NotNull(message = "can't be blank")
	private boolean stock;
	private CategoryDto category;
}
