package kltn.api.output;

import java.util.List;

import kltn.dto.PhotoDTO;
import kltn.dto.RatingDTO;
import lombok.Data;

@Data
public class ProductOutPut {
	private int id;
	private String name;
	private float price;
	private int sale;
	private float priceSale;
	private String description;
	private int categoryId;
	private CategoryOutPut category;
	private List<PhotoDTO> photos;
	private String detail;
	private double weight;
	private int quantitySold;
	private int shopId;
	private String shopName;
	private String shopAvatar;
	private int quantity;
	private int avaiable;
	private double totalStar;
	private List<CommentOuput> comment;
	private List<RatingDTO> rating;
	
	
}

