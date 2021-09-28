package kltn.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.input.UpdateInforProduct;
import kltn.api.input.UpdateDetailProduct;
import kltn.api.output.ProductList;
import kltn.api.output.ProductOutPut;
import kltn.converter.PhotoConverter;
import kltn.converter.ProductConverter;
import kltn.dto.PhotoDTO;
import kltn.dto.ProductDTO;
import kltn.entity.Category;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.CategoryRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.service.IProductService;
import kltn.util.Common;

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
	private CategoryRepository categoryRepository;

	@Autowired
	private PhotoConverter photoConverter;

	@Autowired
	private PhotoService photoService;

	@Override
	public ProductOutPut findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return productConverter.toProductOutPut(
				productRepository.findOneByIdAndIsDeleted(id, false).orElseThrow(() -> new Exception("productId was not found")));
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
			return productRepository.findByCategory_IdAndIsDeleted(pageable, cateroryId, false).map(productConverter::toList);
		}
		return productRepository.findByCategory_IdAndIsDeleted(pageable, cateroryId, false).map(productConverter::toList);
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
		return productRepository.findByShop_IdAndIsDeleted(Common.getIdFromAuth(auth), false, pageable).map(productConverter::toList);
	}

	@Override
	public ProductDTO save(ProductDTO dto, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Shop u = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		Product p = productConverter.toEntity(dto);
		p.setPhotos("");
		p.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> {
			logger.error("Category was not found");
			return new Exception("Category was not found");
		}));
		p.setShop(u);
		p.setDeleted(false);
		int percen = (int) ((dto.getPriceSale() * 100) / dto.getPrice());
		p.setSale(100 - percen);
		p.setQuantitySold(0);
		if (dto.getPhotos() != null) {
			p.setPhotos(photoService.saveOnePhotoProduct(p, dto.getPhotos()));
		}
		Product pro = productRepository.save(p);
		return productConverter.toDTO(pro);
	}

	@Override
	public void deletePhoto(Authentication auth, int photoId, int productId) throws Exception {
//		// TODO Auto-generated method stub
//		Product pr = productRepository.findOneByIdAndShop_Id(productId, Common.getIdFromAuth(auth)).orElseThrow(() -> {
//			logger.error("Auth");
//			return new Exception("Auth");
//		});
//		Optional<Photo> p = photoRepository.findOneByProduct_id(pr.getId());
//		if (p.isPresent()) {
//			File file = new File(constants.rootURL + File.separator + "images" + File.separator + p.get().getName());
//			file.deleteOnExit();
//		}
	}

	@Override
	public void updateInfor(UpdateInforProduct product, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Product p = productRepository.findOneByIdAndShop_IdAndIsDeleted(product.getId(), Common.getIdFromAuth(auth),false)
				.orElseThrow(() -> new Exception("id was not found"));
		p.setName(product.getName());
		p.setPrice(product.getPrice());
		p.setPriceSale(product.getPriceSale());
		p.setDescription(product.getDescription());
		p.setAvaiable(product.getAvaiable());
		p.setWeight(product.getWeight());
		p.setDeleted(p.isDeleted());
		p.setCategory(categoryRepository.findById(product.getCategoryId())
				.orElseThrow(() -> new Exception("categoryId was not found")));
		if (product.getPhotos() != null) {
			p.setPhotos(photoService.updatePhoto(product.getPhotos(), p));
		}
		if (p.getDetail() != null || !p.getDetail().equals("")) {
			p.setDetail(
					generateDetail(p.getDetail(), p.getCategory().getCategory().getName(), p.getCategory().getName()));
		}
		productRepository.save(p);
	}

	@Override
	public void updateDetail(UpdateDetailProduct detail, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Product p = productRepository.findOneByIdAndShop_IdAndIsDeleted(detail.getId(), Common.getIdFromAuth(auth),false)
				.orElseThrow(() -> new Exception("id was not found"));
		p.setDetail(detail.getDetail());
		productRepository.save(p);
	}

	@Override
	public void delete(int id, Authentication auth) {
		// TODO Auto-generated method stub
		Optional<Product> optional = productRepository.findOneByIdAndShop_IdAndIsDeleted(id, Common.getIdFromAuth(auth), false);
		if (optional.isPresent()) {
			Product p = optional.get();
			p.setDeleted(true);
			productRepository.save(p);
		}
	}

	@Override
	public List<PhotoDTO> getListPhotoByShop(Authentication auth) {
		// TODO Auto-generated method stub
		List<Product> pro = productRepository.findByShop_IdAndIsDeleted(Common.getIdFromAuth(auth), false);
		List<PhotoDTO> result = new ArrayList<PhotoDTO>();
		for (Product o : pro) {
			result.addAll(Arrays.asList(o.getPhotos().split(",")).stream().map(photoConverter::toDTO)
					.collect(Collectors.toList()));
		}
		return result;
	}
	
	public void changeData() {
		List<Product> l = productRepository.findAll();
		for(Product e: l) {
			Random rnd = new Random();
			int number = rnd.nextInt(99999999);
			e.setCode(String.format("%08d", number));
			productRepository.save(e);
		}
	}

	private String generateDetail(String detail, String nameParentCate, String childParentCate) {
		String cateOld = detail.split(";")[0];
		String suffix = detail.substring(cateOld.length());
		return nameParentCate + " - " + childParentCate + suffix;
	}
	public static void main(String[] args) {
		
	}
}
