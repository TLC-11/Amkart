package com.amkart.estore.services;

import com.amkart.estore.dtos.OrderDetailsRequest;
import com.amkart.estore.dtos.OrderDto;
import com.amkart.estore.dtos.PageDetailsResponse;

public interface OrderService {

	PageDetailsResponse<OrderDto> getOrderDetailsByUser(int pageno, int pagesize, String sortby, String sortdir, String userid);

	PageDetailsResponse<OrderDto> getAllOrderDetails(int pageno, int pagesize, String sortby, String sortdir);

	OrderDto placeOrder(OrderDetailsRequest orderdetails);

	void cancelOrder(String orderid);

	void cancelItem(int orderitemid);

	OrderDto updateOrderDetails(OrderDto orderdto, String orderid);
}
