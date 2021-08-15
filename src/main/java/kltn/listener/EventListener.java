package kltn.listener;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kltn.entity.Order;
import kltn.event.PushEventUpdateOrderUser;
import kltn.event.AutoUpdateStatus3;
import kltn.event.PushEventUpdateOrderShop;
import kltn.firebase.shop.FirebaseShop;
import kltn.firebase.shop.UpdateStatusOrderShop;
import kltn.firebase.user.FirebaseUser;
import kltn.firebase.user.UpdateStatusOrderUser;
import kltn.repository.OrderRepository;
import kltn.service.IOrderService;
import kltn.util.Common;

@Component
public class EventListener {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private FirebaseUser firebaseUser;
	
	@Autowired
	private FirebaseShop firebaseShop;

	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(AutoUpdateStatus3 e) {

		// then, when you want to schedule a task
		try {
			System.out.println(e.getOrderId() + " : " + e.getShopId());
			System.out.println("sleep");
			Thread.sleep(10000);
			orderService.updateStatusSuccess(e.getOrderId(), e.getShopId(), Common.ORDER_SUCCESS);
			System.out.println("ok");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventUpdateOrderUser e) {
		UpdateStatusOrderUser o = e.getO();
		System.out.println("run");
		if (o.getStatus() == 2) {
			o.setMessage("Đơn hàng " + o.getCode() + " đã được chuyển sang trạng thái đống gói");
		} else if (o.getStatus() == 3) {
			o.setMessage("Đơn hàng " + o.getCode() + " đã được chuyển đến tay người mua");
		}
		firebaseUser.updateOrderStatus(o);
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventUpdateOrderShop e) {
		UpdateStatusOrderShop o = e.getO();
		System.out.println("run");
		if (o.getStatus() == 2) {
			o.setMessage("Đơn hàng " + o.getCode() + " đã được chuyển sang trạng thái đống gói");
		} else if (o.getStatus() == 3) {
			o.setMessage("Đơn hàng " + o.getCode() + " đã được chuyển đến tay người mua");
		}
		firebaseShop.updateOrderStatus(o);
	}
}
