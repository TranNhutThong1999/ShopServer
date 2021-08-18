package kltn.converter;

import java.io.File;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.PhotoDTO;
import kltn.entity.Photo;
import kltn.repository.ProductRepository;
import kltn.util.Constants;

@Component
public class PhotoConverter implements IConverter<Photo, PhotoDTO> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private Constants constant;

	@Override
	public Photo toEntity(PhotoDTO d) {
		// TODO Auto-generated method stub
		// p.setProduct(productRepository.findById(d.getProductId()).orElseThrow(()->
		// new Exception("productId was not found")));
		return modelMapper.map(d, Photo.class);
	}

	@Override
	public PhotoDTO toDTO(Photo s) {
		// TODO Auto-generated method stub
		PhotoDTO p = modelMapper.map(s, PhotoDTO.class);
		p.setLink(constant.showImage + File.separator + "images" + File.separator + s.getName());
		return p;
	}

}
