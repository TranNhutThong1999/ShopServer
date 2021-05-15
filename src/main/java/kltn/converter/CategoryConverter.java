package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.CategoryDTO;
import kltn.dto.SubCategoryDTO;
import kltn.entity.Category;
import kltn.entity.SubCategory;

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
		return modelMapper.map(s, CategoryDTO.class);
	}
	


}
