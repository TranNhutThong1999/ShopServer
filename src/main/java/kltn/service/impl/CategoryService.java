package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kltn.converter.CategoryConverter;
import kltn.dto.CategoryDTO;
import kltn.repository.CategoryRepository;
import kltn.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
	@Autowired
	private CategoryRepository CategoryRepository;
	
	@Autowired
	private CategoryConverter CategoryConverter;

	@Override
	public List<CategoryDTO> findAllCategory() {
		// TODO Auto-generated method stub
		return CategoryRepository.findAll().stream().map(e -> CategoryConverter.toDTO(e)).collect(Collectors.toList());
	}

}
