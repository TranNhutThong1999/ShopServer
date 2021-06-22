package kltn.converter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.api.output.CommentOuput;
import kltn.api.output.ProductList;
import kltn.dto.DetailDTO;
import kltn.dto.ProductDTO;
import kltn.dto.RatingDTO;
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
	
	@Autowired
	private PhotoConverter photoConverter;
	
	@Autowired
	private RatingConverter ratingConverter;
	
	@Override
	public Product toEntity(ProductDTO d) {
		// TODO Auto-generated method stub
		return modelMapper.map(d, Product.class);
	}
	
	public ProductList toList(Product s) {
		// TODO Auto-generated method stub
		ProductList product = modelMapper.map(s, ProductList.class);
		product.setPhoto(photoConverter.toDTO(s.getPhotos().get(0)).getLink());
		double sum = ratingRepository.sumStar(s.getId());
		double realQuantity = ratingRepository.quantityStar(s.getId());
		double quantity = realQuantity == 0 ? 1 : realQuantity;
		product.setTotalStar(sum / quantity);
		return product;
	}
	
	@Override
	public ProductDTO toDTO(Product s) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(5);
		ProductDTO product = modelMapper.map(s, ProductDTO.class);
		CompletableFuture<List<CommentOuput>> futureCmt = CompletableFuture
				.supplyAsync(() -> commentRepository.findAllByProduct_IdAndCommentIdIsNull(s.getId()).stream()
						.map(commentConverter::toDTO).collect(Collectors.toList()), executor);
		
		CompletableFuture<List<RatingDTO>> futureRating = CompletableFuture
				.supplyAsync(() ->s.getRatings().stream().map(ratingConverter::toDTO).collect(Collectors.toList()),executor);
	
		CompletableFuture<Double> futureStar = CompletableFuture.supplyAsync(()->{
			double sum = ratingRepository.sumStar(s.getId());
			double realQuantity = ratingRepository.quantityStar(s.getId());
			double quantity = realQuantity == 0 ? 1 : realQuantity;
			return sum / quantity;
		}, executor);
		product.setPhotos(s.getPhotos().stream().map(photoConverter::toDTO).collect(Collectors.toList()));
		product.setDetail(modelMapper.map(s.getDetail(), DetailDTO.class));
		product.setCategory(categoryConverter.toDTO(s.getCategory()));
		product.setShopId(s.getShop().getId());
		product.setShopName(s.getShop().getNameShop());
		product.setShopAvatar(s.getShop().getAvatar());
		try {
			product.setRating(futureRating.get());
			product.setTotalStar(futureStar.get());
			product.setComment(futureCmt.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 executor.shutdown();
		 return product;
	}

}
