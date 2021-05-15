package kltn.api.output;

import lombok.Data;

@Data
public class ResponseValue {
	private boolean success;
	private Object data;
	private int code;
	private String message;
	
	public ResponseValue(boolean success, int code, String message) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
	}
	
	
}
