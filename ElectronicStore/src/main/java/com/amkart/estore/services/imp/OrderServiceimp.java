package com.amkart.estore.services.imp;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amkart.estore.config.PageDetailsResponseConfig;
import com.amkart.estore.dtos.OrderDetailsRequest;
import com.amkart.estore.dtos.OrderDto;
import com.amkart.estore.dtos.OrderStatus;
import com.amkart.estore.dtos.PageDetailsResponse;
import com.amkart.estore.dtos.PaymentStatus;
import com.amkart.estore.entities.Cart;
import com.amkart.estore.entities.Cart_Item;
import com.amkart.estore.entities.Order;
import com.amkart.estore.entities.Orderitem;
import com.amkart.estore.entities.Product;
import com.amkart.estore.entities.User;
import com.amkart.estore.exceptions.OutOfStockException;
import com.amkart.estore.exceptions.ResourseNotFoundException;
import com.amkart.estore.repositiries.CartRepository;
import com.amkart.estore.repositiries.OrderItemRepository;
import com.amkart.estore.repositiries.OrderRepository;
import com.amkart.estore.repositiries.ProductRepository;
import com.amkart.estore.repositiries.UserRepository;
import com.amkart.estore.services.OrderService;

@Service
public class OrderServiceimp implements OrderService {
	@Autowired
	OrderRepository orderrepo;
	@Autowired
	UserRepository userrepo;
	@Autowired
	ProductRepository prodrepo;
	@Autowired
	OrderItemRepository orderitemrepo;
	@Autowired
	CartRepository cartrepo;
	@Autowired
	PageDetailsResponseConfig pageconfig;
	@Autowired
	ModelMapper mapper;

	@Override
	public OrderDto placeOrder(OrderDetailsRequest orderdetails) {
		// TODO Auto-generated method stub
		String cartid = orderdetails.getCartid();
		String userid = orderdetails.getUserid();
		String billingdetails = orderdetails.getBillingdetails();

		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid user request"));
		Cart cart = cartrepo.findById(cartid).orElseThrow(() -> new ResourseNotFoundException("invalid cart request"));

		List<Cart_Item> cartitems = cart.getCartitems();

		if (cartitems.size() <= 0)
			throw new OutOfStockException("cart is empty");

		Order order = Order.builder().build();

		AtomicReference<Float> totalorderamount = new AtomicReference<>(0.0f);

		List<Orderitem> orderitems = cartitems.stream().map(item -> {
			Product product = item.getProduct();

			if (product.getQunatity() < item.getQuantity())
				throw new OutOfStockException(product.getProductname() + " quantity unavailable");

			Orderitem orderitem = Orderitem.builder().amount(item.getDiscountamount()).quantity(item.getQuantity())
					.product(product).order(order).build();

			totalorderamount.set((totalorderamount.get() + item.getDiscountamount()));

			product.setQunatity(product.getQunatity() - orderitem.getQuantity());
			prodrepo.save(product);

			return orderitem;
		}).collect(Collectors.toList());

		order.setOrderid(UUID.randomUUID().toString());
		order.setOrderstatus(OrderStatus.PENDING);
		order.setPaymentstatus(PaymentStatus.PENDING);
		order.setBillingdetails(billingdetails);
		order.setAmount(totalorderamount.get());
		order.setOrderdate(new Date());
		order.setUser(user);
		order.setOrderitem(orderitems);

		cart.getCartitems().clear();
		cartrepo.save(cart);

		Order save = orderrepo.save(order);
		return mapper.map(save, OrderDto.class);
	}

	@Override
	public void cancelOrder(String orderid) {
		// TODO Auto-generated method stub
		System.out.println(orderid);
		Order order = orderrepo.findById(orderid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid orderid request"));
		System.out.println(order);
		List<Orderitem> orderitems = order.getOrderitem();
		orderitems.stream().forEach(item -> {
			int orderquantity = item.getQuantity();
			Product product = item.getProduct();
			product.setQunatity(orderquantity + product.getQunatity());
			prodrepo.save(product);
		});
		order.getOrderitem().clear();
		order.setPaymentstatus(PaymentStatus.REFUNDED);
		order.setOrderstatus(OrderStatus.CANCELLED);
		order.setDeliverdate(null);
		orderrepo.save(order);
	}

	@Override
	public void cancelItem(int orderitemid) {
		// TODO Auto-generated method stub
		Orderitem orderitem = orderitemrepo.findById(orderitemid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid orderitemid request"));
		int orderquantity = orderitem.getQuantity();
		Product product = orderitem.getProduct();
		product.setQunatity(orderquantity + product.getQunatity());
		prodrepo.save(product);
		Order order = orderitem.getOrder();
		order.setAmount(order.getAmount() - orderitem.getAmount());

		orderitemrepo.delete(orderitem);
		if (order.getAmount() > 0)
			orderrepo.save(order);
		else {
			order.setPaymentstatus(PaymentStatus.REFUNDED);
			order.setOrderstatus(OrderStatus.CANCELLED);
			order.setDeliverdate(null);
			orderrepo.save(order);
		}
	}

	@Override
	public OrderDto updateOrderDetails(OrderDto orderdto, String orderid) {
		// TODO Auto-generated method stub
		Order order = orderrepo.findById(orderid)
				.orElseThrow(() -> new ResourseNotFoundException("invalid orderid request"));
		if (!(orderdto.getBillingdetails() == null))
			order.setBillingdetails(orderdto.getBillingdetails());
		if (!(orderdto.getOrderstatus() == null))
			order.setOrderstatus(orderdto.getOrderstatus());
		if (!(orderdto.getPaymentstatus() == null))
			order.setPaymentstatus(orderdto.getPaymentstatus());
		if (!(orderdto.getDeliverdate() == null))
			order.setDeliverdate(orderdto.getDeliverdate());
		Order save = orderrepo.save(order);
		return mapper.map(save, OrderDto.class);

	}

	@Override
	public PageDetailsResponse<OrderDto> getOrderDetailsByUser(int pageno, int pagesize, String sortby, String sortdir,
			String userid) {
		// TODO Auto-generated method stub
		User user = userrepo.findById(userid).orElseThrow(() -> new ResourseNotFoundException("invalid userid"));
		Sort sort = (sortdir.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pageno, pagesize, sort);
		Page<Order> pages = orderrepo.findByUser(user, pageable);
		return pageconfig.getResponse(pages, OrderDto.class);
	}

	@Override
	public PageDetailsResponse<OrderDto> getAllOrderDetails(int pageno, int pagesize, String sortby, String sortdir) {
		// TODO Auto-generated method stub
		Sort sort = (sortdir.equalsIgnoreCase("desc")) ? (Sort.by(sortby).descending()) : (Sort.by(sortby).ascending());
		Pageable pageable = PageRequest.of(pageno, pagesize, sort);
		Page<Order> pages = orderrepo.findAll(pageable);
		return pageconfig.getResponse(pages, OrderDto.class);

	}

}
