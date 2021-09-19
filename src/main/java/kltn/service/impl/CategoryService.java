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
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryConverter categoryConverter;

	@Override
	public List<CategoryDTO> findAllCategoryParrent() {
		// TODO Auto-generated method stub
		return categoryRepository.findByCategoryIsNull().stream().map(e -> {
			e.setSubCategories(null);
			return categoryConverter.toDTO(e);
		}).collect(Collectors.toList());
	}

	@Override
	public List<CategoryDTO> findAllCategorychildrent(int parentId) {
		// TODO Auto-generated method stub
		return categoryRepository.findByCategory_Id(parentId).stream().map(e -> categoryConverter.toDTO(e))
				.collect(Collectors.toList());
	}

	@Override
	public List<CategoryDTO> findAll() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll().stream().map(e -> categoryConverter.toDTO(e)).collect(Collectors.toList());

	}

//	public void format() {
//		List<Category> list = categoryRepository.findAll();
//		for (Category ca : list) {
//			String name = ca.getName();
//			String result = "";
//			if (name.contains("-")) {
//				String[] item = name.split("-");
//				for (String e : item) {
//					e = e.toLowerCase().trim();
//					String subfix = e.substring(1);
//					e = e.substring(0, 1).toUpperCase() + subfix;
//					result += e + " - ";
//				}
//				result = result.substring(0,result.length()-2).trim();
//				ca.setName(result);
//				System.out.println(result);
//			}else {
//			name = name.toLowerCase().trim();
//			String subfix = name.substring(1);
//			result = name.substring(0, 1).toUpperCase() + subfix;
//			ca.setName(result);
//			System.out.println(result);
//			}
//			categoryRepository.save(ca);
//		}
//	}

	public static void main(String[] args) {

		
	}

}
