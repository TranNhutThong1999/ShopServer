package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.RatingDTO;
import kltn.entity.Rating;
import kltn.repository.ProductRepository;
import kltn.repository.UserRepository;
import kltn.security.CustomUserDetail;

@Component
public class RatingConverter implements IConverter<Rating, RatingDTO> {
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomUserDetail customUserDetail;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Rating toEntity(RatingDTO d) throws Exception {
		// TODO Auto-generated method stub
		Rating entity = modelMapper.map(d, Rating.class);
		entity.setProduct(productRepository.findOneById(d.getProductId()).orElseThrow(()-> new Exception("productId was not found")));
		entity.setUser(userRepository.findById(customUserDetail.getPrincipleId()).get());
		return entity;
	}

	@Override
	public RatingDTO toDTO(Rating s) {
		// TODO Auto-generated method stub
		RatingDTO dto = modelMapper.map(s, RatingDTO.class);
		dto.setProductId(s.getProduct().getId());
		dto.setUserId(s.getUser().getId());
		return dto;
	}

}
