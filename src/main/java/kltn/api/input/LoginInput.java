package kltn.api.input;

import lombok.Data;

@Data 
public class LoginInput {
	private String email;
	private String password;
	public LoginInput(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	
	
}
