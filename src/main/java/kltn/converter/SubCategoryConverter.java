package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.SubCategoryDTO;
import kltn.entity.SubCategory;

@Component
public class SubCategoryConverter implements IConverter<SubCategory, SubCategoryDTO>{
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public SubCategory toEntity(SubCategoryDTO d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubCategoryDTO toDTO(SubCategory s) {
		// TODO Auto-generated method stub
		SubCategoryDTO sub = modelMapper.map(s, SubCategoryDTO.class);
		sub.setCategoryId(s.getCategory().getId());
		return sub;
	}

}
