package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.firebase.UpdateStatusOrder;

public class PushEventUpdateOrderShop extends ApplicationEvent{
	
	private UpdateStatusOrder updateStatusOrder;
	
	public PushEventUpdateOrderShop(Object source, UpdateStatusOrder o) {
		super(source);
		// TODO Auto-generated constructor stub
		this.updateStatusOrder = o;
	}

	public UpdateStatusOrder getO() {
		return updateStatusOrder;
	}

}
