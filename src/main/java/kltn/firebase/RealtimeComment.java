package kltn.firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

public class RealtimeComment {
	private int commentId;
	private int productId;
	private String shopId;
	private Integer parentCommentId;
	private String content;
	private String createDate;
	public RealtimeComment(int commentId, int productId, String shopId, Integer parentCommentId, String content,
			Date createDate) {
		super();
		this.commentId = commentId;
		this.productId = productId;
		this.shopId = shopId;
		this.parentCommentId = parentCommentId;
		this.content = content;
		this.createDate = parse(createDate);
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
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	public void setCreateDate(Date createDate) {
		this.createDate = parse(createDate);
	}
	
	public String parse(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
		return dateFormat.format(date);  
	}
}