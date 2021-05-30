package kltn.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.CategoryDTO;
import kltn.entity.Category;

@Component
public class CategoryConverter implements IConverter<Category, CategoryDTO>{
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Category toEntity(CategoryDTO d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CategoryDTO toDTO(Category s) {
		// TODO Auto-generated method stub
		CategoryDTO ca = modelMapper.map(s, CategoryDTO.class);
		if(s.getCategory() != null)
		ca.setParentId(s.getCategory().getId());
		ca.setSubCategorys(s.getSubCategories().stream().map((e)-> toDTO(e)).collect(Collectors.toList()));
		return ca;
	}
	


}
