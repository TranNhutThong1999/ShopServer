package kltn.firebase;

import java.util.Date;

import lombok.Data;

public class RealtimeComment {
	private int commentId;
	private int productId;
	private int userId;
	private Integer parentCommentId;
	private String content;
	private String createDate;
	public RealtimeComment(int commentId, int productId, int userId, Integer parentCommentId,
			String content, String createDate) {
		super();
		this.commentId = commentId;
		this.productId = productId;
		this.userId = userId;
		this.parentCommentId = parentCommentId;
		this.content = content;
		this.createDate = createDate;
	}
	public RealtimeComment() {
		super();
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Integer getParentCommentId() {
		return parentCommentId;
	}
	public void setParentCommentId(Integer parentCommentId) {
		this.parentCommentId = parentCommentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "RealtimeCommentUser [commentId=" + commentId + ", productId=" + productId + ", userId=" + userId
				+ ", parentCommentId=" + parentCommentId + ", content=" + content + ", createDate=" + createDate + "]";
	}
	
}