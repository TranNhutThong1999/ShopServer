package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.firebase.RealtimeComment;

public class PushEventCommentShop extends ApplicationEvent {

	private RealtimeComment comment;

	public PushEventCommentShop(Object source, RealtimeComment c) {
		super(source);
		// TODO Auto-generated constructor stub
		this.comment = c;
	}

	public RealtimeComment getComment() {
		return comment;
	}

}
