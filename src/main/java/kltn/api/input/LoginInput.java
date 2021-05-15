package kltn.api.input;

import lombok.Data;

@Data 
public class LoginInput {
	private String username;
	private String password;
	public LoginInput(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	
}
