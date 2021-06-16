package kltn.api.input;

import lombok.Data;

@Data
public class UploadFileInput {
	private String base64String;
	private String name;
}	
