package kltn.event;

import org.springframework.context.ApplicationEvent;

public class AutoUpdateStatus3 extends ApplicationEvent {

	private int orderId;

	private String shopId;

	public AutoUpdateStatus3(Object source, int orderId, String shopId) {
		super(source);
		// TODO Auto-generated constructor stub
		this.orderId = orderId;
		this.shopId = shopId;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getShopId() {
		return shopId;
	}

}
