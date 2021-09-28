package kltn.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.output.ListOrder;
import kltn.converter.OrderConverter;
import kltn.dto.OrderDTO;
import kltn.entity.Notification;
import kltn.entity.Order;
import kltn.event.PushEventUpdateOrderUser;
import kltn.firebase.UpdateStatusOrder;
import kltn.event.AutoUpdateStatus3;
import kltn.event.PushEventNotiOrderShop;
import kltn.event.PushEventNotiOrderUser;
import kltn.event.PushEventUpdateOrderShop;
import kltn.repository.OrderRepository;
import kltn.service.IOrderService;
import kltn.util.Common;

@Service
public class OrderService implements IOrderService {
	Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderConverter orderConverter;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public List<ListOrder> getListOrder(Authentication auth) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "id");
		List<Order> order = orderRepository.findByShop_Id(Common.getIdFromAuth(auth), sort);
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
		System.out.println("before run sleep");
		applicationEventPublisher.publishEvent(new PushEventUpdateOrderShop(this, dataS));
		// push change status 2 to 3
		applicationEventPublisher.publishEvent(new AutoUpdateStatus3(this, o.getId(), o.getShop().getId()));
		Notification user = new Notification();
		user.setAvatar("product" + or.getDetail().get(0).getProduct().getPhotos().split(",")[0]);
		user.setType(Common.NOTI_ORDER);
		user.setOrder(o);
		user.setStatus(o.getStatus());
		user.setTime(Common.parse(o.getCreatedDate()));
		user.setUser(o.getUser());
		applicationEventPublisher.publishEvent(new PushEventNotiOrderUser(this, user));
		Notification shopN = new Notification();
		shopN.setAvatar("product" + or.getDetail().get(0).getProduct().getPhotos().split(",")[0]);
		shopN.setType(Common.NOTI_ORDER);
		shopN.setOrder(o);
		shopN.setStatus(o.getStatus());
		shopN.setTime(Common.parse(o.getCreatedDate()));
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

//			for (Item i : o.getDetail()) {
//				Action ac = new Action();
//				ac.setName(Common.ACTION_BOUGHT);
//				ac.setUser(o.getUser());
//				ac.setProduct(i.getProduct());
//				ac.setIsRating(0);
//				actionRepository.save(ac);
//			}

			Notification noti = new Notification();
			noti.setAvatar("product" + or.getDetail().get(0).getProduct().getPhotos().split(",")[0]);
			noti.setType(Common.NOTI_ORDER);
			noti.setOrder(o);
			noti.setStatus(o.getStatus());
			noti.setTime(Common.parse(o.getCreatedDate()));
			noti.setUser(o.getUser());
			applicationEventPublisher.publishEvent(new PushEventNotiOrderUser(this, noti));

			Notification notiS = new Notification();
			notiS.setAvatar("product" + or.getDetail().get(0).getProduct().getPhotos().split(",")[0]);
			notiS.setType(Common.NOTI_ORDER);
			notiS.setOrder(o);
			notiS.setStatus(o.getStatus());
			notiS.setTime(Common.parse(o.getCreatedDate()));
			notiS.setShop(o.getShop());
			applicationEventPublisher.publishEvent(new PushEventNotiOrderShop(this, notiS));
		}

	}

	@Override
	public void updateCancelStatus(int orderId, String reason, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Optional<Order> o = orderRepository.findOneByIdAndShop_Id(orderId, Common.getIdFromAuth(auth));
		Order or = null;
		if (o.isPresent()) {
			or = o.get();
			if (or.getStatus() != Common.ORDER_SUCCESS && or.getStatus() != Common.ORDER_CANCEL) {
				or.setStatus(Common.ORDER_CANCEL);
				or.setReasonCancel(reason);
				or = orderRepository.save(or);
				UpdateStatusOrder dataU = new UpdateStatusOrder(or.getId(), or.getUser().getId(), or.getOrderCode(),
						or.getStatus(), or.getShop().getId(), or.getCreatedDate());
				// realtime user
				applicationEventPublisher.publishEvent(new PushEventUpdateOrderUser(this, dataU));

				Notification noti = new Notification();
				noti.setAvatar("product" + or.getDetail().get(0).getProduct().getPhotos().split(",")[0]);
				noti.setType(Common.NOTI_ORDER);
				noti.setOrder(or);
				noti.setStatus(or.getStatus());
				noti.setTime(Common.parse(or.getCreatedDate()));
				noti.setUser(or.getUser());
				applicationEventPublisher.publishEvent(new PushEventNotiOrderUser(this, noti));
			}
		}
	}

	 public void updateData() {
		List<Order> order = orderRepository.findAll();
		Calendar calendar = Calendar.getInstance();
		Date date1 = null;
		String orderTime = null;
		String dayOfWeek = null;
		for (Order o : order) {
			try {
				date1 = new SimpleDateFormat("yyyy-MM-dd").parse(o.getCreatedDate().toString());
				calendar.setTime(date1);
				calendar.add(Calendar.DATE, -3);
//				System.out.println( o.getCreatedDate().toString());
//				System.out.println(calendar.getTime());
//				System.out.println(calendar.get(Calendar.MONTH));
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				switch (day) {
				case Calendar.MONDAY:
					dayOfWeek = "Thứ Hai";
					break;
				case Calendar.TUESDAY:
					dayOfWeek = "Thứ Ba";
					break;
				case Calendar.WEDNESDAY:
					dayOfWeek = "Thứ Tư";
					break;
				case Calendar.THURSDAY:
					dayOfWeek = "Thứ Năm";
					break;
				case Calendar.FRIDAY:
					dayOfWeek = "Thứ Sáu";
					break;
				case Calendar.SATURDAY:
					dayOfWeek = "Thứ Bảy";
					break;
				case Calendar.SUNDAY:
					dayOfWeek = "Chủ Nhật";
					break;
				}
				String[] list = o.getCreatedDate().toString().split("-");
				orderTime = dayOfWeek + ", ngày " +calendar.get(Calendar.DAY_OF_MONTH) + " tháng " + (calendar.get(Calendar.MONTH)+1) + ", " + calendar.get(Calendar.YEAR);
				System.out.println(orderTime);
				o.setOrderTime(orderTime);
				orderRepository.save(o);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		Date date1 = null;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-09 11:48:10.967000");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.setTime(date1);
		calendar.add(Calendar.DATE, -3);
		String[] list = "2021-05-28 11:48:10.967000".split("-");
		// calendar.set(Integer.valueOf(list[0]), Integer.valueOf(list[1]),
		// Integer.valueOf(list[2].substring(0,2)));
//		calendar.set(Calendar.MONTH, 5);
//		calendar.set(Calendar.YEAR, 2021);
//		calendar.set(Calendar.DAY_OF_WEEK, 15);
		// calendar.add(Calendar.DATE, -5);
		// int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.MONTH)+1);
	}
}
