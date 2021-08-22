package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.entity.Notification;

public class PushEventNotiOrderShop extends ApplicationEvent {
	private Notification noti;

	public PushEventNotiOrderShop(Object source, Notification noti) {
		super(source);
		// TODO Auto-generated constructor stub
		this.noti = noti;
	}

	public Notification getNoti() {
		return noti;
	}
}
