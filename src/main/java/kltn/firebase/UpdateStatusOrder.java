package kltn.firebase;

public class UpdateStatusOrder {
	private int orderId;
	private int userId;
	private String code;
	private int status;
	private int shopId;
	private String createDate;
	public UpdateStatusOrder(int orderId, int userId, String code, int status, int shopId, String createDate) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.code = code;
		this.status = status;
		this.shopId = shopId;
		this.createDate = createDate;
	}
	public UpdateStatusOrder() {
		super();
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}