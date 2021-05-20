package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.converter.ProductConverter;
import kltn.dto.ProductDTO;
import kltn.entity.Detail;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.entity.SubCategory;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.repository.SubCategoryRepository;
import kltn.repository.UserRepository;
import kltn.service.IProductService;

@Service
public class ProductService implements IProductService{
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductConverter productConverter;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
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

	@Override
	public ProductDTO save(ProductDTO dto, String userName) {
		// TODO Auto-generated method stub
		Shop u = shopRepository.findOneByUserName(userName).get();
		Product p = productConverter.toEntity(dto);
		p.setDetail(modelMapper.map(dto.getDetail(),Detail.class));
		p.setSubCategory(subCategoryRepository.findById(dto.getSubCategory().getId()).get());
		p.setShop(u);
		return productConverter.toDTO(productRepository.save(p));
	}

}
