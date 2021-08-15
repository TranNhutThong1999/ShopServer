package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.firebase.user.UpdateStatusOrderUser;

public class PushEventUpdateOrderUser extends ApplicationEvent{
	
	private UpdateStatusOrderUser updateStatusOrder;
	
	public PushEventUpdateOrderUser(Object source, UpdateStatusOrderUser o) {
		super(source);
		// TODO Auto-generated constructor stub
		this.updateStatusOrder = o;
	}

	public UpdateStatusOrderUser getO() {
		return updateStatusOrder;
	}

}
