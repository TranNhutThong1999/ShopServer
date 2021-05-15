package kltn.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.ShopDTO;
import kltn.entity.Shop;
import kltn.repository.FollowingRepository;
import kltn.repository.RoleRepository;

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
		shop.setRole(s.getRole().getName());
		shop.setFollow(followingRepository.countUserFollow(s.getId()));
	//	shop.setProducts(s.getProducts().stream().map(productConverter::toDTO).collect(Collectors.toList()));
		return shop;
	}

}
