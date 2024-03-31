package com.amkart.estore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Orderitem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderitemid;
	private int quantity;
	private float amount;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productid")
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderid")
	private Order order;

}
