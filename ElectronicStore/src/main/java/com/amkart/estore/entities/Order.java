package com.amkart.estore.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amkart.estore.dtos.OrderStatus;
import com.amkart.estore.dtos.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "order_details")
public class Order {
	@Id
	private String orderid;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderstatus;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentstatus;
	private String billingdetails;
	private float amount;
	private Date orderdate;
	private Date deliverdate;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	private User user;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Orderitem> orderitem = new ArrayList<>();

}
