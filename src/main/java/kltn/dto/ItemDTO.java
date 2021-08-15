package kltn.dto;

import lombok.Data;

@Data
public class ItemDTO extends AbstractDTO{
	private int itemId;
	private int productId;
	private int quantity;
	private float price;
	private String photo;
	private String name;
}
