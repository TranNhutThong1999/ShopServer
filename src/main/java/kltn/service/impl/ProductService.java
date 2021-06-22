package kltn.service.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.rpc.context.AttributeContext.Auth;

import kltn.api.output.ProductList;
import kltn.converter.PhotoConverter;
import kltn.converter.ProductConverter;
import kltn.dto.ProductDTO;
import kltn.entity.Category;
import kltn.entity.Detail;
import kltn.entity.Photo;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.CategoryRepository;
import kltn.repository.PhotoRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.security.MyShop;
import kltn.service.IProductService;
import kltn.util.Constants;

@Service
public class ProductService implements IProductService {
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

	@Autowired
	private Constants constants;

	@Autowired
	private PhotoRepository photoRepository;

	@Override
	public ProductDTO findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return productConverter
				.toDTO(productRepository.findById(id).orElseThrow(() -> new Exception("productId was not found")));
	}

	@Override
	public Page<ProductList> findByCategoryLimit(int cateroryId, int pageSize, int pageNumber) throws Exception {
		// TODO Auto-generated method stub
		Category ca = categoryRepository.findById(cateroryId)
				.orElseThrow(() -> new Exception("categoryId was not found"));
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		if (ca.getCategory() != null) {
			return productRepository.findByCategory_Id(pageable, cateroryId).map(productConverter::toList);
		}
		return productRepository.findByCategory_Category_Id(pageable, cateroryId).map(productConverter::toList);
	}

	@Override
	public List<ProductList> findRandomLimit(int limit) {
		// TODO Auto-generated method stub
		return productRepository.findRandomLimit(limit).stream().map(productConverter::toList)
				.collect(Collectors.toList());
	}

	@Override
	public Page<ProductList> findByShopId(int shopId, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return productRepository.findByShop_id(shopId, pageable).map(productConverter::toList);
	}

//	@Override
//	public Page<ProductDTO> findProductByShop_Id(int id, int pageSize, int pageNumber) {
//		// TODO Auto-generated method stub
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//		return productRepository.findByShop_Id(id, pageable).map(productConverter::toList);
//	}

	@Override
	public ProductDTO save(ProductDTO dto, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Shop u = shopRepository.findById(getIdFromAuth(auth)).get();
		Product p = productConverter.toEntity(dto);
		p.setDetail(modelMapper.map(dto.getDetail(), Detail.class));
		p.setCategory(categoryRepository.findById(dto.getCategory().getId())
				.orElseThrow(() -> new Exception("Category was not found")));
		p.setShop(u);
		int percen = (dto.getPrice() * 100) / dto.getPriceSale();
		p.setSale(percen);
//		p.setPhotos(dto.getPhotos().stream().map((x)-> {
//			Photo pho = photoConverter.toEntity(x);
//			pho.setProduct(p);
//			return pho;
//		}).collect(Collectors.toList()));
		return productConverter.toDTO(productRepository.save(p));
	}

	@Override
	public void deletePhoto(Authentication auth, int photoId, int productId) throws Exception {
		// TODO Auto-generated method stub
		Product pr = productRepository.findOneByIdAndShop_Id(productId, getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("Auth"));
		Photo p = photoRepository.findOneByProduct_id(pr.getId());
		File file = new File(constants.rootURL + File.separator + "images" + File.separator + p.getName());
		file.deleteOnExit();
	}

	private int getIdFromAuth(Authentication auth) {
		MyShop u = (MyShop) auth.getPrincipal();
		return u.getId();
	}
}
