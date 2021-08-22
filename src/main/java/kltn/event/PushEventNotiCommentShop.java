package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.entity.Notification;

public class PushEventNotiCommentShop extends ApplicationEvent{
	private Notification noti;

	public PushEventNotiCommentShop(Object source, Notification noti) {
		super(source);
		// TODO Auto-generated constructor stub
		this.noti = noti;
	}

	public Notification getNoti() {
		return noti;
	}
}
