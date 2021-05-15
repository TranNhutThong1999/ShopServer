package kltn.dto;

import lombok.Data;

@Data
public class RatingDTO {
	private int star;
	private int userId;
	private int productId;
}
