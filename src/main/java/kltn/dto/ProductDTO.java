package kltn.dto;

import java.util.List;

import kltn.api.output.CommentOuput;
import lombok.Data;

@Data
public class ProductDTO  {
	private String id;
	private String name;
	private float price;
	private int sale;
	private float priceSale;
	private String description;
	private int categoryId;
	private List<PhotoDTO> photos;
	private String detail;
	private double weight;
	private int quantitySold;
	private int shopId;
	private String shopName;
	private String shopAvatar;
	private int avaiable;
	private double totalStar;
	private List<CommentOuput> comment;
	private List<RatingDTO> rating;
}
