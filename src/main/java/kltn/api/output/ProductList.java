package kltn.api.output;


import lombok.Data;

@Data
public class ProductList {
	private int id;
	private String name;
	private double price;
	private int sale;
	private double priceSale;
	private int quantitySold;
	private String photo;
	private double totalStar;
	private int totalComment;
}
