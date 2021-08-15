package kltn.event;

import org.springframework.context.ApplicationEvent;

public class AutoUpdateStatus3 extends ApplicationEvent {

	private int orderId;

	private int shopId;

	public AutoUpdateStatus3(Object source, int orderId, int shopId) {
		super(source);
		// TODO Auto-generated constructor stub
		this.orderId = orderId;
		this.shopId = shopId;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getShopId() {
		return shopId;
	}

}
