package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.converter.PhotoConverter;
import kltn.converter.ProductConverter;
import kltn.dto.ProductDTO;
import kltn.entity.Detail;
import kltn.entity.Photo;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.CategoryRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
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
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private PhotoConverter photoConverter;
	
	@Override
	public ProductDTO findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return productConverter.toDTO(productRepository.findById(id).orElseThrow(()-> new Exception("productId was not found")));
	}

	@Override
	public Page<ProductDTO> findByCategoryLimit(int cateroryId, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findByCategory_Id(pageable, cateroryId).map(productConverter::toDTO);
	}
	
	@Override
	public List<ProductDTO> findRandomLimit(int limit) {
		// TODO Auto-generated method stub
		return productRepository.findRandomLimit(limit).stream().map(productConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public Page<ProductDTO> findByShopId(int shopId, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findByShop_id(shopId, pageable).map(productConverter::toDTO);
	}

	@Override
	public Page<ProductDTO> findProductByShop(String userName, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findByShop_UserName(userName, pageable).map(productConverter::toDTO);
	}

	@Override
	public ProductDTO save(ProductDTO dto, String userName) throws Exception {
		// TODO Auto-generated method stub
		Shop u = shopRepository.findOneByUserName(userName).get();
		Product p = productConverter.toEntity(dto);
		p.setDetail(modelMapper.map(dto.getDetail(),Detail.class));
		p.setCategory(categoryRepository.findById(dto.getCategory().getId()).orElseThrow(()-> new Exception("Category was not found")));
		p.setShop(u);
		int percen = (Integer.valueOf(dto.getPrice())*100) / Integer.valueOf(dto.getPriceSale());
		dto.setSale(percen);
		p.setPhotos(dto.getPhotos().stream().map((x)-> {
			Photo pho = photoConverter.toEntity(x);
			pho.setProduct(p);
			return pho;
		}).collect(Collectors.toList()));
		return productConverter.toDTO(productRepository.save(p));
	}

}
