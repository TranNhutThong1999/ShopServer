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
import kltn.entity.Action;
import kltn.entity.Item;
import kltn.entity.Notification;
import kltn.entity.Order;
import kltn.entity.Payment;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.event.PushEventUpdateOrderUser;
import kltn.firebase.UpdateStatusOrder;
import kltn.event.AutoUpdateStatus3;
import kltn.event.PushEventNotiOrderShop;
import kltn.event.PushEventNotiOrderUser;
import kltn.event.PushEventUpdateOrderShop;
import kltn.repository.ActionRepository;
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

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private ActionRepository actionRepository;

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
		UpdateStatusOrder data = new UpdateStatusOrder(o.getId(), o.getUser().getId(), o.getOrderCode(), o.getStatus(),
				o.getShop().getId(), o.getCreatedDate());
		// push realtime user
		applicationEventPublisher.publishEvent(new PushEventUpdateOrderUser(this, data));
		// push realtime shop
		UpdateStatusOrder dataS = new UpdateStatusOrder(o.getId(), o.getShop().getId(), o.getOrderCode(), o.getStatus(),
				o.getShop().getId(), o.getCreatedDate());
		applicationEventPublisher.publishEvent(new PushEventUpdateOrderShop(this, dataS));
		// push change status 2 to 3
		if (status == Common.ORDER_TRANSPORT && status != Common.ORDER_SUCCESS)
			applicationEventPublisher.publishEvent(new AutoUpdateStatus3(this, orderId, Common.getIdFromAuth(auth)));

		Shop shop = o.getShop();
		Notification user = new Notification();
		user.setAvatar(shop.getAvatar());
		user.setType(Common.NOTI_ORDER);
		user.setOrderId(o.getId());
		user.setStatus(o.getStatus());
		user.setTime(Common.parse(o.getCreatedDate()));
		user.setOrderCode(o.getOrderCode());
		user.setUser(o.getUser());
		applicationEventPublisher.publishEvent(new PushEventNotiOrderUser(this, user));
		Notification shopN = new Notification();
		shopN.setAvatar(shop.getAvatar());
		shopN.setType(Common.NOTI_ORDER);
		shopN.setOrderId(o.getId());
		shopN.setStatus(o.getStatus());
		shopN.setTime(Common.parse(o.getCreatedDate()));
		shopN.setOrderCode(o.getOrderCode());
		shopN.setShop(o.getShop());
		applicationEventPublisher.publishEvent(new PushEventNotiOrderShop(this, shopN));
	}

	@Override
	@Transactional
	public void updateStatusSuccess(int orderId, String shopId, int status) {
		// TODO Auto-generated method stub
		Optional<Order> order = orderRepository.findOneByIdAndShop_Id(orderId, shopId);
		Order or = null;
		if (order.isPresent()) {
			or = order.get();
			or.setStatus(status);
			Order o = orderRepository.save(or);
			UpdateStatusOrder dataU = new UpdateStatusOrder(o.getId(), o.getUser().getId(), o.getOrderCode(),
					o.getStatus(), o.getShop().getId(), o.getCreatedDate());
			// realtime user
			applicationEventPublisher.publishEvent(new PushEventUpdateOrderUser(this, dataU));
			// realtime shop
			UpdateStatusOrder dataS = new UpdateStatusOrder(o.getId(), o.getShop().getId(), o.getOrderCode(),
					o.getStatus(), o.getShop().getId(), o.getCreatedDate());
			applicationEventPublisher.publishEvent(new PushEventUpdateOrderShop(this, dataS));

			for (Item i : o.getDetail()) {
				Action ac = new Action();
				ac.setName(Common.ACTION_BOUGHT);
				ac.setUser(o.getUser());
				ac.setProduct(i.getProduct());
				ac.setIsRating(0);
				actionRepository.save(ac);
			}

			Shop shop = o.getShop();
			Notification noti = new Notification();
			noti.setAvatar(shop.getAvatar());
			noti.setType(Common.NOTI_ORDER);
			noti.setOrderId(o.getId());
			noti.setStatus(o.getStatus());
			noti.setTime(Common.parse(o.getCreatedDate()));
			noti.setOrderCode(o.getOrderCode());
			noti.setUser(o.getUser());
			applicationEventPublisher.publishEvent(new PushEventNotiOrderUser(this, noti));
			
			Notification notiS = new Notification();
			notiS.setAvatar(shop.getAvatar());
			notiS.setType(Common.NOTI_ORDER);
			notiS.setOrderId(o.getId());
			notiS.setStatus(o.getStatus());
			notiS.setTime(Common.parse(o.getCreatedDate()));
			notiS.setOrderCode(o.getOrderCode());
			notiS.setShop(o.getShop());
			applicationEventPublisher.publishEvent(new PushEventNotiOrderShop(this, notiS));
		}

	}

}
