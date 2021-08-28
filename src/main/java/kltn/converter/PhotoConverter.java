package kltn.converter;

import java.io.File;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.PhotoDTO;
import kltn.util.Constants;

@Component
public class PhotoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Constants constant;

//	@Override
//	public Photo toEntity(PhotoDTO d) {
//		// TODO Auto-generated method stub
//		// p.setProduct(productRepository.findById(d.getProductId()).orElseThrow(()->
//		// new Exception("productId was not found")));
//		return modelMapper.map(d, Photo.class);
//	}

	public PhotoDTO toDTO(String name) {
		PhotoDTO p = new PhotoDTO();
		p.setLink(constant.showImage + File.separator + "images" + File.separator + name);
		p.setName(name);
		return p;
	}

	public String toLink(String name) {
		return constant.showImage + File.separator + "images" + File.separator + name;
	}

	public String toLinkCategory(String name) {
		return constant.showImage + File.separator + "images" + File.separator + "category" + File.separator + name;
	}

	public String toLinkAvatarUser(String name) {
		if(name.substring(0, 4).equalsIgnoreCase("http")) return name;
		return constant.showImage + File.separator + "images" + File.separator + "avatar" + File.separator + "user"
				+ File.separator + name;
	}

	public String toLinkAvatarShop(String name) {
		return constant.showImage + File.separator + "images" + File.separator + "avatar" + File.separator + "shop"
				+ File.separator + name;
	}
}
