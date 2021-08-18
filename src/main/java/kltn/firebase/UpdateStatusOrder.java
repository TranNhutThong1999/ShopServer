package kltn.firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateStatusOrder {
	private int orderId;
	private String userId;
	private String code;
	private int status;
	private String shopId;
	private String createDate;
	public UpdateStatusOrder(int orderId, String userId, String code, int status, String shopId, Date createDate) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.code = code;
		this.status = status;
		this.shopId = shopId;
		this.createDate = parse(createDate);
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
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
//	public static void main(String[] args) {
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
//		System.out.println(dateFormat.format(new Date())); 
//	}

}