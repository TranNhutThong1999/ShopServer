package kltn.dto;

import lombok.Data;

@Data
public class PhotoDTO {
	private int id;
	private String link;
	private String name;
	private String base64String;
}
