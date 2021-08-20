package kltn.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.rpc.context.AttributeContext.Auth;

import kltn.api.input.UpdateInforProduct;
import kltn.api.input.UpdateDetailProduct;
import kltn.api.output.ProductList;
import kltn.api.output.ProductOutPut;
import kltn.converter.PhotoConverter;
import kltn.converter.ProductConverter;
import kltn.dto.ProductDTO;
import kltn.entity.Category;
import kltn.entity.Photo;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.CategoryRepository;
import kltn.repository.PhotoRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.security.MyShop;
import kltn.service.IProductService;
import kltn.util.Common;
import kltn.util.Constants;

@Service
public class ProductService implements IProductService {
	Logger logger = LoggerFactory.getLogger(ProductService.class);

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

	@Autowired
	private PhotoService photoService;

	@Override
	public ProductOutPut findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return productConverter.toProductOutPut(
				productRepository.findById(id).orElseThrow(() -> new Exception("productId was not found")));
	}

	@Override
	public Page<ProductList> findByCategoryLimit(int cateroryId, int pageSize, int pageNumber) throws Exception {
		// TODO Auto-generated method stub
		Category ca = categoryRepository.findById(cateroryId).orElseThrow(() -> {
			logger.error("categoryId was not found");
			return new Exception("categoryId was not found");
		});
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
	public Page<ProductList> findByShopId(Authentication auth, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "id");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return productRepository.findByShop_id(Common.getIdFromAuth(auth), pageable).map(productConverter::toList);
	}

	@Override
	public ProductDTO save(ProductDTO dto, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Shop u = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		Product p = productConverter.toEntity(dto);
		p.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> {
			logger.error("Category was not found");
			return new Exception("Category was not found");
		}));
		p.setShop(u);
		int percen = (int) ((dto.getPriceSale() * 100) / dto.getPrice());
		p.setSale(100 - percen);
		p.setQuantitySold(0);
		if (dto.getPhotos().size() > 0) {
			p.setPhotos(photoService.saveOnePhotoProduct(p, dto.getPhotos()));
		}
		Product pro = productRepository.save(p);
		return productConverter.toDTO(pro);
	}

	@Override
	public void deletePhoto(Authentication auth, int photoId, int productId) throws Exception {
		// TODO Auto-generated method stub
		Product pr = productRepository.findOneByIdAndShop_Id(productId, Common.getIdFromAuth(auth)).orElseThrow(() -> {
			logger.error("Auth");
			return new Exception("Auth");
		});
		Optional<Photo> p = photoRepository.findOneByProduct_id(pr.getId());
		if (p.isPresent()) {
			File file = new File(constants.rootURL + File.separator + "images" + File.separator + p.get().getName());
			file.deleteOnExit();
		}
	}

	@Override
	public void updateInfor(UpdateInforProduct product, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Product p = productRepository.findOneByIdAndShop_Id(product.getId(), Common.getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("id was not found"));
		p.setName(product.getName());
		p.setPrice(product.getPrice());
		p.setPriceSale(product.getPriceSale());
		p.setDescription(product.getDescription());
		p.setAvaiable(product.getAvaiable());
		p.setWeight(product.getWeight());
		p.setCategory(categoryRepository.findById(product.getCategoryId())
				.orElseThrow(() -> new Exception("categoryId was not found")));
		if (product.getPhotos() != null) {
			p.setPhotos(photoService.updatePhoto(product.getPhotos(), p));
		}
		productRepository.save(p);
	}

	@Override
	public void updateDetail(UpdateDetailProduct detail, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Product p = productRepository.findOneByIdAndShop_Id(detail.getId(), Common.getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("id was not found"));
		p.setDetail(detail.getDetail());
		productRepository.save(p);
	}

	@Override
	public void delete(int id, Authentication auth) {
		// TODO Auto-generated method stub
		Optional<Product> p = productRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth));
		if (p.isPresent())
			System.out.println("vao");
		productRepository.delete(p.get());
		p.get().getPhotos().stream().forEach((x) -> {
			File f = new File(constants.rootURL + File.separator + "/images" + File.separator + x.getName());
			f.deleteOnExit();
		});
	}

}
