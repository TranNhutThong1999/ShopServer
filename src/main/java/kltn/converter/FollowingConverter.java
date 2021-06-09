package kltn.converter;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.FollowingDTO;
import kltn.entity.Following;
import kltn.repository.ShopRepository;
import kltn.repository.UserRepository;
import kltn.security.CustomUserDetail;

@Component
public class FollowingConverter implements IConverter<Following, FollowingDTO>{
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private CustomUserDetail customUserDetail;
	
	@Override
	public Following toEntity(FollowingDTO d) throws Exception {
//		// TODO Auto-generated method stub
//		Following entity = modelMapper.map(d, Following.class);
//		entity.setUser(userRepository.findById(customUserDetail.getPrincipleId()).get());
//		entity.setShop(shopRepository.findById(d.getShopId()).orElseThrow(()-> new Exception("shopId was not found")));
//		return entity;
		return null;
	}

	@Override
	public FollowingDTO toDTO(Following s) {
		// TODO Auto-generated method stub
		FollowingDTO dto = modelMapper.map(s, FollowingDTO.class);
		dto.setUserId(s.getUser().getId());
		dto.setShopId(s.getShop().getId());
		return dto;
	}

}
