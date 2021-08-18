package kltn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.output.ListOrder;
import kltn.converter.OrderConverter;
import kltn.converter.ProductConverter;
import kltn.dto.OrderDTO;
import kltn.entity.Item;
import kltn.entity.Order;
import kltn.entity.Payment;
import kltn.entity.Product;
import kltn.event.PushEventUpdateOrderUser;
import kltn.firebase.UpdateStatusOrder;
import kltn.event.AutoUpdateStatus3;
import kltn.event.PushEventUpdateOrderShop;
import kltn.repository.ItemRepository;
import kltn.repository.OrderRepository;
import kltn.repository.ProductRepository;
import kltn.repository.UserRepository;
import kltn.service.IOrderService;
import kltn.util.Common;

@Service
public class OrderService implements IOrderService {
	Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderConverter orderConverter;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private PaymentRepository paymentRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ApplicationEventPublisher ApplicationEventPublisher;

//	@Override
//	public void save(OrderDTO order, Authentication auth) throws Exception {
//		// TODO Auto-generated method stub
//		logger.info("create order");
//		Order or = orderConverter.toEntity(order);
//		or.setOrderCode(generateOrderCode());
//		or.setUser(userRepository.findOneById(Common.getIdFromAuth(auth)).get());
//		or.setStatus(Common.ORDER_PROCESS);
//		Payment payment = new Payment();
//		payment.setPamentOnline(order.getIsPaymentOnline() == 1 ? true : false);
//		if (order.getIsPaymentOnline() == 1) {
//			payment.setPayment(true);
//		} else {
//			payment.setPayment(false);
//		}
//		payment.setPaymentName(order.getPaymentName());
//
//		or.setPayment(payment);
//		int price = 0;
//		List<ItemDTO> listOrderDetail = order.getProduct();
//		for (ItemDTO dto : listOrderDetail) {
//			Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new Exception("Item was not found"));
//			Product p = item.getProduct();
//			item.setCart(null);
//			item.setOrder(or);
//			item.setPrice(p.getPriceSale());
//			
//			if (p.getAvaiable() - item.getQuantity() < 0) {
//				throw new Exception("min avaiable < 0");
//			}
//			p.setAvaiable(p.getAvaiable() - dto.getQuantity());
//			p.setQuantitySold(p.getQuantitySold()+1);
//			itemRepository.save(item);
//			productRepository.save(p);
//			price += p.getPriceSale() * item.getQuantity();
//		}
//		or.setTempPrice(price);
//		or.setTotalMoney(price + or.getFeeShip());
//		orderRepository.save(or);
//	}

	@Override
	public List<ListOrder> getListOrder(Authentication auth) {
		// TODO Auto-generated method stub
		List<Order> order = orderRepository.findByShop_Id(Common.getIdFromAuth(auth));
		List<ListOrder> result = new ArrayList<ListOrder>();
		for (Order o : order) {
			result.add(orderConverter.tolist(o.getDetail().get(0), o));
		}
		return result;
	}

	private String generateOrderCode() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999999);
		return String.format("%09d", number);
	}

	@Override
	public OrderDTO getOrder(int id, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		return orderConverter.toDTO(orderRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("id was not found")));
	}

	@Override
	public void updateStatus(int orderId, Authentication auth, int status) throws Exception {
		// TODO Auto-generated method stub
		Order or = orderRepository.findOneByIdAndShop_Id(orderId, Common.getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("id was not found"));
		or.setStatus(status);
		Order o = orderRepository.save(or);
		UpdateStatusOrder data = new UpdateStatusOrder(o.getId(), o.getUser().getId(), o.getOrderCode(), o.getStatus(), o.getShop().getId(), o.getCreatedDate().toString());
		//push realtime user
		ApplicationEventPublisher.publishEvent(new PushEventUpdateOrderUser(this, data));
		//push realtime shop
		ApplicationEventPublisher.publishEvent(new PushEventUpdateOrderShop(this, data));
		//push change status 2 to 3
		ApplicationEventPublisher.publishEvent(new AutoUpdateStatus3(this, orderId, Common.getIdFromAuth(auth)));
	}

	@Override
	@Transactional
	public void updateStatusSuccess(int orderId, int shopId, int status) {
		// TODO Auto-generated method stub
		Optional<Order> order = orderRepository.findOneByIdAndShop_Id(orderId, shopId);
		if (order.isPresent()) {
			Order or = order.get();
			or.setStatus(status);
			Order o = orderRepository.save(or);
			UpdateStatusOrder data = new UpdateStatusOrder(o.getId(), o.getUser().getId(), o.getOrderCode(), o.getStatus(), o.getShop().getId(), o.getCreatedDate().toString());
		//realtime user
			ApplicationEventPublisher.publishEvent(new PushEventUpdateOrderUser(this, data));
		//realtime shop
			ApplicationEventPublisher.publishEvent(new PushEventUpdateOrderShop(this, data));
		}

	}

}
