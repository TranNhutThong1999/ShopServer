package kltn.converter;

import java.io.File;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.ItemDTO;
import kltn.entity.Item;
import kltn.util.Constants;

@Component
public class ItemConverter implements IConverter<Item, ItemDTO> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Constants constant;

	@Override
	public Item toEntity(ItemDTO d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemDTO toDTO(Item s) {
		// TODO Auto-generated method stub
		ItemDTO i = new ItemDTO();
		i.setId(s.getId());
		i.setName(s.getProduct().getName());
		if (s.getProduct().getPhotos() != null)
			i.setPhoto(constant.showImage + File.separator + "images" + File.separator
					+ s.getProduct().getPhotos().split(",")[0]);
		i.setQuantity(s.getQuantity());
		i.setProductId(s.getProduct().getId());
		i.setPrice(s.getPrice());
		return i;
	}

}
