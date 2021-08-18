package kltn.dto;

import lombok.Data;

@Data
public class RatingDTO {
	private int star;
	private String userId;
	private int productId;
}
