package kltn.api.output;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorOutput {
	private Date time;
	private HttpStatus status;
	private String messages;
	
	public ErrorOutput(Date time, HttpStatus status, String messages) {
		super();
		this.time = time;
		this.status = status;
		this.messages = messages;
	}
	
}
