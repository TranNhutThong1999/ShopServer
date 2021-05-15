package kltn.service;

import java.util.List;

import kltn.dto.CategoryDTO;

public interface ICategoryService {
	List<CategoryDTO> findAllCategory();
	
}
