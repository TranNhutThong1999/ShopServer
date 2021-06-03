package kltn.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.DetailDTO;
import kltn.dto.ProductDTO;
import kltn.entity.Product;
import kltn.repository.CommentRepository;
import kltn.repository.ProductRepository;
import kltn.repository.RatingRepository;
import kltn.service.IPhotoService;

@Component
public class ProductConverter implements IConverter<Product, ProductDTO>{
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IPhotoService photoService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryConverter categoryConverter;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentConverter commentConverter;
	
	@Override
	public Product toEntity(ProductDTO d) {
		// TODO Auto-generated method stub
		return modelMapper.map(d, Product.class);
	}

	@Override
	public ProductDTO toDTO(Product s) {
		// TODO Auto-generated method stub
		ProductDTO product = modelMapper.map(s, ProductDTO.class);
		product.setPhotos(photoService.findByProduct_Id(s.getId()));
		product.setDetail(modelMapper.map(s.getDetail(), DetailDTO.class));
		product.setCategory(categoryConverter.toDTO(s.getCategory()));
		product.setShopId(s.getShop().getId());
		product.setShopName(s.getShop().getNameShop());
		product.setShopAvatar(s.getShop().getAvatar());
		double sum = ratingRepository.sumStar(s.getId());
		double quantity = ratingRepository.quantityStar(s.getId()) == 0 ? 1 : ratingRepository.quantityStar(s.getId());
		product.setTotalStar( sum / quantity ); 
		product.setComment(commentRepository.findAllByProduct_IdAndCommentIdIsNull(s.getId()).stream().map(commentConverter::toDTO).collect(Collectors.toList()));
		return product;
	}

}
