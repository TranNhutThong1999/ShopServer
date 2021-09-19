package kltn.api.output;

import lombok.Data;

@Data
public class ListOrder {
	private String orderCode;
	private int productId;
	private int orderId;
	private String photo;
	private String name;
	private int quantity;
	private float price;
	private int status;
	
}
