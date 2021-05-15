package kltn.api.output;

import kltn.dto.UserDTO;
import lombok.Data;

@Data
public class LoginOutput {
	private String token;
	private UserDTO user;
	public LoginOutput(String token, UserDTO user) {
		super();
		this.token = token;
		this.user = user;
	}
}

