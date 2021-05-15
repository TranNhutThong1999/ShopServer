package kltn.api.output;

import java.util.Map;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Errors {
	
	private int code;
	private Map<String,String> messages;
	public Errors(int code, Map<String, String> messages) {
		super();
		this.code = code;
		this.messages = messages;
	}
}