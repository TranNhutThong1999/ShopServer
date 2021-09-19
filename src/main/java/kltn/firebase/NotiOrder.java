package kltn.firebase;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kltn.entity.Notification;
import lombok.Data;

public class NotiOrder {
	private String id;
	private int type;
	private String avatar;
	private String title;
	private String subTitle;
	private String time;
//	private int productId;
	private String orderCode;
	private int status;
	private int orderId;
	
	public NotiOrder(Notification noti) {
		this.id = noti.getId();
		this.type =noti.getType();
		this.avatar = noti.getAvatar();
		this.title =noti.getTitle();
		this.subTitle = noti.getSubTitle();
		this.time = noti.getTime();
		this.orderCode = noti.getOrder().getOrderCode();
		this.status = noti.getStatus();
		this.orderId = noti.getOrder().getId();
	}

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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

//	public int getProductId() {
//		return productId;
//	}
//
//	public void setProductId(int productId) {
//		this.productId = productId;
//	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}

