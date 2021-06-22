package kltn.dto;


import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import kltn.annotation.EmailExist;
import kltn.annotation.ShopExist;
import lombok.Data;

@Data
public class ShopDTO extends AbstractDTO {
	private String nameBoss;
	private String avatar;
	private String nameShop;
	
	@ShopExist
	@NotBlank
	private String userName;
	private String phone;
	
	@EmailExist
	@NotBlank
	private String email;
	private String code;
	private String hotLine;
	private String website;
	private int follow;
	

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private String location;
	private ProvinceDTO prodincial;
	private DistrictDTO district;
	private WardsDTO wards;
	
	private String token;
}
