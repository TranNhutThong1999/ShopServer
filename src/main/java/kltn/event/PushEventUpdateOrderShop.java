package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.firebase.shop.UpdateStatusOrderShop;

public class PushEventUpdateOrderShop extends ApplicationEvent{
	
	private UpdateStatusOrderShop updateStatusOrder;
	
	public PushEventUpdateOrderShop(Object source, UpdateStatusOrderShop o) {
		super(source);
		// TODO Auto-generated constructor stub
		this.updateStatusOrder = o;
	}

	public UpdateStatusOrderShop getO() {
		return updateStatusOrder;
	}

}
