package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.DistrictDTO;
import kltn.dto.ProvinceDTO;
import kltn.dto.ShopDTO;
import kltn.dto.WardsDTO;
import kltn.entity.Shop;

@Component
public class ShopConverter implements IConverter<Shop, ShopDTO> {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductConverter productConverter;

	@Override
	public Shop toEntity(ShopDTO d) {
		// TODO Auto-generated method stub
		return modelMapper.map(d, Shop.class);
	}

	@Override
	public ShopDTO toDTO(Shop s) {
		// TODO Auto-generated method stub
		ShopDTO shop = modelMapper.map(s, ShopDTO.class);
	
		if (s.getAddress() != null) {
			shop.setAddressId(s.getAddress().getId());
			shop.setLocation(s.getAddress().getLocation());
			shop.setProvince(modelMapper.map(s.getAddress().getProvince(), ProvinceDTO.class));
			shop.setDistrict(modelMapper.map(s.getAddress().getDistrict(), DistrictDTO.class));
			shop.setWards(modelMapper.map(s.getAddress().getWards(), WardsDTO.class));
		}
		return shop;
	}

}
