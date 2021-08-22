package kltn.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kltn.event.PushEventUpdateOrderUser;
import kltn.firebase.FirebaseShop;
import kltn.firebase.FirebaseUser;
import kltn.event.AutoUpdateStatus3;
import kltn.event.PushEventCommentShop;
import kltn.event.PushEventCommentUser;
import kltn.event.PushEventNotiCommentShop;
import kltn.event.PushEventNotiCommentUser;
import kltn.event.PushEventNotiOrderShop;
import kltn.event.PushEventNotiOrderUser;
import kltn.event.PushEventUpdateOrderShop;
import kltn.service.IOrderService;
import kltn.util.Common;
import kltn.util.Constants;

@Component
public class EventListener {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private FirebaseUser firebaseUser;
	
	@Autowired
	private FirebaseShop firebaseShop;
	
	@Autowired
	private Constants constants;
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(AutoUpdateStatus3 e) {

		// then, when you want to schedule a task
		try {
			System.out.println("sleep " +constants.getSleep()+"s");
			Thread.sleep(constants.getSleep());
			orderService.updateStatusSuccess(e.getOrderId(), e.getShopId(), Common.ORDER_SUCCESS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventUpdateOrderUser e) {
		firebaseUser.updateOrderStatus(e.getO());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventUpdateOrderShop e) {
		firebaseShop.updateOrderStatus(e.getO());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventCommentUser e) {
		firebaseUser.updateRealtimeCommentUser(e.getComment());
	}

	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventCommentShop e) {
		firebaseShop.updateRealtimeCommentShop(e.getComment());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventNotiOrderUser e) {
		firebaseUser.updateNotificationOrder(e.getNoti());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventNotiOrderShop e) {
		firebaseShop.updateNotificationOrder(e.getNoti());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventNotiCommentUser e) {
		firebaseUser.updateNotificationComment(e.getNoti());
	}
	
	@Async
	@org.springframework.context.event.EventListener
	@Transactional
	public void handleEventListener(PushEventNotiCommentShop e) {
		firebaseShop.updateNotificationComment(e.getNoti());
	}
}
