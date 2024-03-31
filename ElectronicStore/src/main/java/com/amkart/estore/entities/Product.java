package com.amkart.estore.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Products")
public class Product {
	@Id
	private String productid;
	@Column(unique = true, nullable = false)
	private String productname;
	@Column(name = "description")
	private String productdesc;
	@Column(nullable = false)
	private float price;
	@Column(nullable = false)
	private float discountprice;
	@Column(nullable = false)
	private int qunatity;
	@Column(nullable = false)
	private Date addeddate;
	@Column(nullable = false)
	private boolean islive;
	@Column(nullable = false)
	private boolean stock;
	private String productimg;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category")
	private Category category;

}
