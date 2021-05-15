package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.converter.ProductConverter;
import kltn.dto.ProductDTO;
import kltn.entity.Product;
import kltn.repository.ProductRepository;
import kltn.service.IProductService;

@Service
public class ProductService implements IProductService{
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductConverter productConverter;
	
	@Override
	public ProductDTO findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return productConverter.toDTO(productRepository.findById(id).orElseThrow(()-> new Exception("productId was not found")));
	}

	@Override
	public Page<ProductDTO> findBySubCategoryLimit(int subCateroryId, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		System.out.println(subCateroryId +"|"+ pageNumber +"|" + pageSize);
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findBySubCategory_Id(pageable, subCateroryId).map(productConverter::toDTO);
	}
	
	@Override
	public List<ProductDTO> findRandomLimit(int limit) {
		// TODO Auto-generated method stub
		return productRepository.findRandomLimit(limit).stream().map(productConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public Page<ProductDTO> findProductByShop(String userName, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findByShop_UserName(userName, pageable).map(productConverter::toDTO);
	}

}
