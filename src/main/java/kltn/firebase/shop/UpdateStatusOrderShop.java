package kltn.firebase.shop;

public class UpdateStatusOrderShop {
	private String id;
	private int userId;
	private int shopId;
	private String code;
	private int status;
	private String message;
	public UpdateStatusOrderShop( int userId, int shopId, String code, int status) {
		super();
		this.userId = userId;
		this.code = code;
		this.status = status;
		this.shopId =shopId;
	}
	
	public UpdateStatusOrderShop() {
		super();
	
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}


}