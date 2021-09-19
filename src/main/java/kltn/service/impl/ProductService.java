package kltn.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import kltn.entity.Item;
import kltn.entity.Order;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.CategoryRepository;
import kltn.repository.ItemRepository;
import kltn.repository.OrderRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
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
	private CategoryRepository categoryRepository;

	@Autowired
	private PhotoConverter photoConverter;

	@Autowired
	private Constants constants;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderRepository orderRepository;

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
		return productRepository.findByShop_Id(Common.getIdFromAuth(auth), pageable).map(productConverter::toList);
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
		if (p.getDetail() != null || !p.getDetail().equals("")) {
			p.setDetail(
					generateDetail(p.getDetail(), p.getCategory().getCategory().getName(), p.getCategory().getName()));
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
	@Transactional
	public void delete(int id, Authentication auth) {
		// TODO Auto-generated method stub
		Optional<Product> p = productRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth));
		if (p.isPresent()) {
			List<Item> i = itemRepository.findByProduct_Id(id);
			for(Item item : i) {
				Order o = item.getOrder();
				System.out.println(o.getDetail().size());
				if (o.getDetail().size()==1) {
					orderRepository.delete(o);
				}
			}
			productRepository.delete(p.get());
			for (String x : p.get().getPhotos().split(",")) {
				if (x.equals(""))
					continue;
				File f = new File(constants.rootURL + File.separator + "/images" + File.separator + x);
				f.deleteOnExit();
			}
		}
	}

	@Override
	public List<PhotoDTO> getListPhotoByShop(Authentication auth) {
		// TODO Auto-generated method stub
		List<Product> pro = productRepository.findByShop_Id(Common.getIdFromAuth(auth));
		List<PhotoDTO> result = new ArrayList<PhotoDTO>();
		for (Product o : pro) {
			result.addAll(Arrays.asList(o.getPhotos().split(",")).stream().map(photoConverter::toDTO)
					.collect(Collectors.toList()));
		}
		return result;
	}

	private String generateDetail(String detail, String nameParentCate, String childParentCate) {
		String cateOld = detail.split(";")[0];
		String suffix = detail.substring(cateOld.length());
		return nameParentCate + " - " + childParentCate + suffix;
	}
}
