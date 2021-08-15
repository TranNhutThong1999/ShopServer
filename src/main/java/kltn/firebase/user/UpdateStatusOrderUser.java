package kltn.firebase.user;

public class UpdateStatusOrderUser {
	private String id;
	private int userId;
	private String code;
	private int status;
	private String message;
	public UpdateStatusOrderUser( int userId, String code, int status) {
		super();
		this.userId = userId;
		this.code = code;
		this.status = status;
	}
	
	public UpdateStatusOrderUser() {
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
	@Override
	public String toString() {
		return "UpdateStatusOrder [id=" + id + ", userId=" + userId + ", code=" + code + ", status=" + status
				+ ", message=" + message + "]";
	}

}