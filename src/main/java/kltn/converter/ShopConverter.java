package kltn.converter;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.DistrictDTO;
import kltn.dto.ProvincialDTO;
import kltn.dto.ShopDTO;
import kltn.dto.WardsDTO;
import kltn.entity.Shop;
import kltn.repository.FollowingRepository;

@Component
public class ShopConverter implements IConverter<Shop, ShopDTO> {
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FollowingRepository followingRepository;
	
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
		shop.setFollow(followingRepository.countUserFollow(s.getId()));
		shop.setLocation(s.getAddress().getLocation());
		shop.setProdincial(modelMapper.map(s.getAddress().getProvincial(), ProvincialDTO.class));
		shop.setDistrict(modelMapper.map(s.getAddress().getDistrict(), DistrictDTO.class));
		shop.setWards(modelMapper.map(s.getAddress().getWards(), WardsDTO.class));
		return shop;
	}

}
