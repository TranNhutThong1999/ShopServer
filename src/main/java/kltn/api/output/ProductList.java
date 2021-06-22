package kltn.api.output;


import lombok.Data;

@Data
public class ProductList {
	private int id;
	private String name;
	private int price;
	private int sale;
	private int priceSale;
	private int quantitySold;
	private String photo;
	private double totalStar;
}
