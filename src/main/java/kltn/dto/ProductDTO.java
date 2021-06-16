package kltn.dto;

import java.util.List;

import kltn.api.output.CommentOuput;
import lombok.Data;

@Data
public class ProductDTO extends AbstractDTO {
	private String name;
	private int price;
	private int sale;
	private String description;
	//private ProductStatus status;
	private int priceSale;
	private CategoryDTO category;
	private List<PhotoDTO> photos;
	private DetailDTO detail;
	private int quantitySold;
	private int shopId;
	private String shopName;
	private String shopAvatar;
	private int quantity;
	private double totalStar;
	private List<CommentOuput> comment;
	private List<RatingDTO> rating;
}
