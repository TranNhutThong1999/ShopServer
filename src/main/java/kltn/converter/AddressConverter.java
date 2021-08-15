package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.AddressDTO;
import kltn.dto.DistrictDTO;
import kltn.dto.ProvinceDTO;
import kltn.dto.WardsDTO;
import kltn.entity.Address;

@Component
public class AddressConverter implements IConverter<Address, AddressDTO>{

	@Autowired
	private ModelMapper modelmapper;
	
	@Override
	public Address toEntity(AddressDTO d)  {
		// TODO Auto-generated method stub
		Address a = modelmapper.map(d, Address.class);
	//	a.setProvince(modelmapper.map(d, destination));
		return null;
	}

	@Override
	public AddressDTO toDTO(Address s) {
		AddressDTO o = modelmapper.map(s, AddressDTO.class);
		o.setLocation(s.getLocation());
		o.setProvince(modelmapper.map(s.getProvince(), ProvinceDTO.class));
		o.setDistrict(modelmapper.map(s.getDistrict(),DistrictDTO.class));
		o.setWards(modelmapper.map(s.getWards(), WardsDTO.class));
		return o;
	}

}
