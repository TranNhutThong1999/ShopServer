package kltn.service;

import java.util.List;

import kltn.dto.CategoryDTO;

public interface ICategoryService {
	List<CategoryDTO> findAll();
	List<CategoryDTO> findAllCategoryParrent();
	List<CategoryDTO> findAllCategorychildrent(int parentId);
}
