package kltn.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import kltn.annotation.ShopExist;
import lombok.Data;

@Data
public class ShopDTO extends AbstractDTO {
	private String nameBoss;
	private String avatar;
	private String address;
	private String nameShop;
	
	@ShopExist
	private String userName;
	private String phone;
	
	private String gmail;
	private String code;
	private String hotLine;
	private String website;
	private int follow;

	//private List<ProductDTO> products;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String role;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String token;
}
