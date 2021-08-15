package kltn.dto;

import lombok.Data;

@Data
public class DetailDTO extends AbstractDTO{
	private String provideByShop;
	private String trademark;
	private String brandOrigin;
	private String useTutorial;
	private String madeBy;
	private String material;
	private String model;
	private String vat;
	private String guarantee;
	private double weight;
}
