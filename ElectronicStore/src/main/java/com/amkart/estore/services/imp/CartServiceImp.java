package com.amkart.estore.services.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amkart.estore.dtos.CartDetailsResponse;
import com.amkart.estore.dtos.CartDto;
import com.amkart.estore.entities.Cart;
import com.amkart.estore.entities.Cart_Item;
import com.amkart.estore.entities.Product;
import com.amkart.estore.entities.User;
import com.amkart.estore.exceptions.ResourseNotFoundException;
import com.amkart.estore.repositiries.CartItemRepository;
import com.amkart.estore.repositiries.CartRepository;
import com.amkart.estore.repositiries.ProductRepository;
import com.amkart.estore.repositiries.UserRepository;
import com.amkart.estore.services.CartService;

@Service
public class CartServiceImp implements CartService {

	@Autowired
	UserRepository userrepo;
	@Autowired
	ProductRepository prodrepo;
	@Autowired
	CartRepository cartrepo;
	@Autowired
	CartItemRepository cartitemrepo;
	@Autowired
	ModelMapper mapper;

	@Override
	public CartDto addItemTocart(String userid, String productid, int quantity) {
		// TODO Auto-generated method stub
//		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid userid"));
//		Product product = prodrepo.findById(productid)
//				.orElseThrow(() -> new ResourseNotFoundException("invalid productid"));
//		if (!(quantity > 0 && quantity <= product.getQunatity()))
//			throw new OutOfStockException("invalid quantity request");
//
//		Cart cart = null;
//		try {
//			cart = cartrepo.findByUser(user).get();
//		} catch (NoSuchElementException e) {
//			cart = new Cart();
//			cart.setId(userid + "cart");
//			cart.setUser(user);
//		}
//
//		AtomicReference<Boolean> updated = new AtomicReference<>(false);
//		List<Cart_Item> cartitems = cart.getCartitems();
//		List<Cart_Item> updatedcartitems = cartitems.stream().map(item -> {
//			if (item.getProduct().getProductid().equals(productid)) {
//				item.setQuantity(quantity);
//				item.setActualamount(quantity * product.getPrice());
//				item.setDiscountamount(quantity * product.getDiscountprice());
//				item.setDiscount((quantity * product.getPrice()) - (quantity * product.getDiscountprice()));
//				updated.set(true);
//			}
//			return item;
//		}).collect(Collectors.toList());
//		cart.setCartitems(updatedcartitems);
//		if (!updated.get()) {
//			Cart_Item item = Cart_Item.builder().quantity(quantity).actualamount(quantity * product.getPrice())
//					.discountamount(quantity * product.getDiscountprice())
//					.discount((quantity * product.getPrice()) - (quantity * product.getDiscountprice()))
//					.product(product).cart(cart).build();
//			cart.getCartitems().add(item);
//		}
//
//		Cart updatecart = cartrepo.save(cart);
//		return mapper.map(updatecart, CartDto.class);
		if (quantity <= 0) {
			throw new ResourseNotFoundException("invalid userid");
		}

		// fetch the product
		Product product = prodrepo.findById(productid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid userid"));
		// fetch the user from db
		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid userid"));

		Cart cart = null;
		try {
			cart = cartrepo.findByUser(user).get();
		} catch (NoSuchElementException e) {
			cart = new Cart();

			cart.setId(userid + "cart");

		}

		// perform cart operations
		// if cart items already present; then update
		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		List<Cart_Item> items = cart.getCartitems();
		items = items.stream().map(item -> {

			if (item.getProduct().getProductid().equals(productid)) {
				item.setQuantity(quantity);
				item.setActualamount(quantity * product.getPrice());
				item.setDiscountamount(quantity * product.getDiscountprice());
				item.setDiscount((quantity * product.getPrice()) - (quantity * product.getDiscountprice()));
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());

//	        cart.setItems(updatedItems);

		// create items
		if (!updated.get()) {

			Cart_Item item = Cart_Item.builder().quantity(quantity).actualamount(quantity * product.getPrice())
					.discountamount(quantity * product.getDiscountprice())
					.discount((quantity * product.getPrice()) - (quantity * product.getDiscountprice()))
					.product(product).cart(cart).build();
			cart.getCartitems().add(item);
		}

		cart.setUser(user);
		Cart updatedCart = cartrepo.save(cart);
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void clearCart(String userid) {
		// TODO Auto-generated method stub
		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid userid"));
		Cart cart = cartrepo.findByUser(user).orElseThrow(() -> new ResourseNotFoundException("no item in cart"));
		cart.getCartitems().clear();
		cartrepo.save(cart);

	}

	@Override
	public void removeItem(int itemid) {
		// TODO Auto-generated method stub
		Cart_Item item = cartitemrepo.findById(itemid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid request to remove item"));
		cartitemrepo.delete(item);
	}

	@Override
	public CartDetailsResponse getDetails(String userid) {
		// TODO Auto-generated method stub
		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid userid"));
		Cart cart = cartrepo.findByUser(user).orElseThrow(() -> new ResourseNotFoundException("no item in cart"));
		List<Cart_Item> cartitems = cart.getCartitems();
		float totalprice = 0;
		float totalpriceafterdiscount = 0;
		int quantity = 0;
		float discount = 0;
		List<String> productname = new ArrayList<>();
		Iterator<Cart_Item> iterator = cartitems.iterator();
		while (iterator.hasNext()) {
			Cart_Item item = iterator.next();
			productname.add(item.getProduct().getProductname());
			quantity += item.getQuantity();
			totalprice = item.getActualamount() + totalprice;
			totalpriceafterdiscount = item.getDiscountamount() + totalpriceafterdiscount;
			discount = item.getDiscount() + discount;

		}
		CartDetailsResponse resp = CartDetailsResponse.builder().productname(productname).quantity(quantity)
				.totalprice(totalprice).discountprice(totalpriceafterdiscount).discount(discount).build();
		return resp;
	}

}
