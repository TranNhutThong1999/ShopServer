package kltn.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kltn.dto.ProductDTO;

@Service
public interface IProductService {
	ProductDTO findOneById(int id) throws Exception;
	Page<ProductDTO> findBySubCategoryLimit(int subCateroryId, int pageSize, int pageNumber);
	List<ProductDTO> findRandomLimit(int limit);
	Page<ProductDTO> findProductByShop(String userName, int pageSize, int pageNumber);
	ProductDTO save(ProductDTO dto, String userName);
}
