package com.amkart.estore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Cart_Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int itemid;
	private int quantity;
	private float actualamount;
	private float discountamount;
	private float discount;
	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "cartid")
	private Cart cart;

}
