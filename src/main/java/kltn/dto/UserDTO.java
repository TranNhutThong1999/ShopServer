package kltn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDTO extends AbstractDTO{
	private String firstName;
	private String lastName;
	private String fullName;
	private String pictureURL;
	
	private String phone;
	
	private String gmail;
	private String dayOfBirth;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private boolean enabled; //defaul: false is not verify
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private boolean gender; //true is boy
	
	private String role;
	private String token;
	
	public UserDTO() {
		super();
	}
	
	
}
