package kltn.dto;

import lombok.Data;

@Data
public class DetailDTO extends AbstractDTO{
	private String provideByShop;
	private String trademark;
	private String madeBy;
	private String size;
	private String color;
	private String material;
}
