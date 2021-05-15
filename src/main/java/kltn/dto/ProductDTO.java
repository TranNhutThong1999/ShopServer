package kltn.dto;

import java.util.List;

import kltn.entity.ProductStatus;
import lombok.Data;

@Data
public class ProductDTO extends AbstractDTO {
	private String name;
	private String price;
	private String sale;
	private String description;
	//private ProductStatus status;
	private SubCategoryDTO subCategory;
	private List<PhotoDTO> photos;
	private DetailDTO detail;
	private int shopId;
	private String shopName;
	private String shopAvatar;
	private int quantity;
	private double totalStar;
}