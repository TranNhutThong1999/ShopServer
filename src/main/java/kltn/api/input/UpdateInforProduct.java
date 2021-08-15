package kltn.api.input;

import java.util.List;

import kltn.dto.PhotoDTO;
import lombok.Data;

@Data
public class UpdateInforProduct {
	private int id;
	private String name;
	private int price;
	private int priceSale;
	private String description;
	private double weight;
	private int categoryId;
	private List<PhotoDTO> photos;
}
