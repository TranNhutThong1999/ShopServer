package kltn.dto;



import lombok.Data;

@Data
public class AddressDTO {
	private String location;
	private ProvinceDTO province;
	private DistrictDTO district;
	private WardsDTO Wards;
	
}
