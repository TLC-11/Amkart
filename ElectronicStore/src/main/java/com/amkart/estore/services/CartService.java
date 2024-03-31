package com.amkart.estore.services;

import com.amkart.estore.dtos.CartDetailsResponse;
import com.amkart.estore.dtos.CartDto;

public interface CartService {

	CartDto addItemTocart(String userid, String productid, int quantity);

	void clearCart(String userid);

	CartDetailsResponse getDetails(String userid);

	void removeItem(int itemid);

}
