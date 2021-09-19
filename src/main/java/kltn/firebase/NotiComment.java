package kltn.firebase;

import kltn.entity.Notification;
import lombok.Data;

public class NotiComment {
	private String id;
	private int type;
	private String avatar;
	private String title;
	private String subTitle;
	private String time;
	private int productId;
	private int commentId;
	
	public NotiComment(Notification noti) {
		this.id = noti.getId();
		this.type = noti.getType();
		this.avatar = noti.getAvatar();
		this.title = noti.getTitle();
		this.subTitle = noti.getSubTitle();
		this.time = noti.getTime();
		if(noti.getComment() != null) {
			this.productId = noti.getComment().getProduct().getId();
			this.commentId =noti.getComment().getId();
		}else {
			this.productId = noti.getReply().getComment().getProduct().getId();
			this.commentId =noti.getReply().getId();
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	@Override
	public String toString() {
		return "NotiCommentUser [type=" + type + ", avatar=" + avatar + ", title=" + title + ", subTitle=" + subTitle
				+ ", time=" + time + ", productId=" + productId + ", commentId=" + commentId + "]";
	}
	
}
