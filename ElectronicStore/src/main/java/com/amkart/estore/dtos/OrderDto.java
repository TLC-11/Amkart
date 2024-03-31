package com.amkart.estore.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class OrderDto {
	private String orderid;
	private OrderStatus orderstatus;
	private PaymentStatus paymentstatus;
	private String billingdetails;
	private float amount;
	private Date orderdate;
	private Date deliverdate;
	private UserDto user;
	private List<OrderitemDto> orderitem = new ArrayList<>();

}
