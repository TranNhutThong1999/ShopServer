package kltn.api.input;

import kltn.dto.DistrictDTO;
import kltn.dto.ProvinceDTO;
import kltn.dto.WardsDTO;
import lombok.Data;
@Data
public class ShopDetail {
	private String id;
	private String name;
	private String code;
	private String hotLine;
	private String website;
	
	private String location;
	private ProvinceDTO province;
	private DistrictDTO district;
	private WardsDTO wards;
}
