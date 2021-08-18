package kltn.event;

import org.springframework.context.ApplicationEvent;

import kltn.entity.Comment;
import kltn.firebase.RealtimeComment;

public class PushEventCommentUser extends ApplicationEvent{
	
	private RealtimeComment comment;
	
	public PushEventCommentUser(Object source, RealtimeComment c) {
		super(source);
		// TODO Auto-generated constructor stub
		this.comment = c;
	}

	public RealtimeComment getComment() {
		return comment;
	}

}
