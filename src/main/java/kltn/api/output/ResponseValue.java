package kltn.api.output;

import org.springframework.http.HttpStatus;

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
	
	public ResponseValue() {}

	public void setCode(HttpStatus accepted) {
		// TODO Auto-generated method stub
		this.code = accepted.value();
	};
}
