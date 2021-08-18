package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.firebase.UpdateStatusOrder;

public class PushEventUpdateOrderUser extends ApplicationEvent{
	
	private UpdateStatusOrder updateStatusOrder;
	
	public PushEventUpdateOrderUser(Object source, UpdateStatusOrder o) {
		super(source);
		// TODO Auto-generated constructor stub
		this.updateStatusOrder = o;
	}

	public UpdateStatusOrder getO() {
		return updateStatusOrder;
	}

}
