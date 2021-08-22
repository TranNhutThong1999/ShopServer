package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.entity.Notification;

public class PushEventNotiCommentUser extends ApplicationEvent{
	private Notification noti;

	public PushEventNotiCommentUser(Object source, Notification noti) {
		super(source);
		// TODO Auto-generated constructor stub
		this.noti = noti;
	}

	public Notification getNoti() {
		return noti;
	}
}
