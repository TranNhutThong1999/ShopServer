package kltn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.input.UpdateDetailProduct;
import kltn.api.input.UpdateInforProduct;
import kltn.api.output.ProductList;
import kltn.api.output.ProductOutPut;
import kltn.dto.ProductDTO;

@Service
public interface IProductService {
	ProductOutPut findOneById(int id) throws Exception;

	Page<ProductList> findByCategoryLimit(int cateroryId, int pageSize, int pageNumber) throws Exception;

	List<ProductList> findRandomLimit(int limit);

//	Page<ProductDTO> findProductByShop_Id(int id, int pageSize, int pageNumber);
	ProductDTO save(ProductDTO dto, Authentication auth) throws Exception;

	Page<ProductList> findByShopId(Authentication auth, int pageSize, int pageNumber);
	
	void deletePhoto(Authentication auth, int photoId, int productId) throws Exception;
	
	void updateInfor(UpdateInforProduct product, Authentication auth) throws Exception;
	
	void updateDetail(UpdateDetailProduct detail, Authentication auth) throws Exception;
	
	void delete(int id, Authentication auth);
}
