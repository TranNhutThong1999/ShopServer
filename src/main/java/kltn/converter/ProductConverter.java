package kltn.converter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.usage.record.AllTime.Category;

import kltn.api.output.CategoryOutPut;
import kltn.api.output.CommentOuput;
import kltn.api.output.ProductList;
import kltn.api.output.ProductOutPut;
import kltn.dto.DetailDTO;
import kltn.dto.ProductDTO;
import kltn.dto.RatingDTO;
import kltn.entity.Comment;
import kltn.entity.Product;
import kltn.repository.CommentRepository;
import kltn.repository.ProductRepository;
import kltn.repository.RatingRepository;
import kltn.service.IPhotoService;
import kltn.util.Constants;

@Component
public class ProductConverter implements IConverter<Product, ProductDTO> {
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
		if (s.getPhotos() != null) {
			product.setPhoto(photoConverter.toLink(s.getPhotos().split(",")[0]));
		}
		double sum = ratingRepository.sumStar(s.getId());
		double realQuantity = ratingRepository.quantityStar(s.getId());
		double quantity = realQuantity == 0 ? 1 : realQuantity;
		product.setTotalStar(sum / quantity);
		int totalComment = 0;
		if (s.getComments().size() > 0) {
			for (Comment c : s.getComments()) {
				totalComment++;
			}
		}

		product.setTotalComment(totalComment);
		return product;
	}

	@Override
	public ProductDTO toDTO(Product s) {
		return null;
	}

	public ProductOutPut toProductOutPut(Product s) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(5);
		ProductOutPut product = modelMapper.map(s, ProductOutPut.class);
		CompletableFuture<List<CommentOuput>> futureCmt = CompletableFuture.supplyAsync(() -> commentRepository
				.findAllByProduct_Id(s.getId()).stream().map(commentConverter::toDTO).collect(Collectors.toList()),
				executor);

//		CompletableFuture<List<RatingDTO>> futureRating = CompletableFuture
//				.supplyAsync(() ->s.getRatings().stream().map(ratingConverter::toDTO).collect(Collectors.toList()),executor);

		CompletableFuture<Double> futureStar = CompletableFuture.supplyAsync(() -> {
			double sum = ratingRepository.sumStar(s.getId());
			double realQuantity = ratingRepository.quantityStar(s.getId());
			double quantity = realQuantity == 0 ? 1 : realQuantity;
			return sum / quantity;
		}, executor);
		if (s.getPhotos() != null) {
			List<String> l = Arrays.asList(s.getPhotos().split(","));
			product.setPhotos(l.stream().map(photoConverter::toDTO).collect(Collectors.toList()));
		}
		CategoryOutPut cateOutPut = modelMapper.map(s.getCategory().getCategory(), CategoryOutPut.class);
		if (cateOutPut.getImage() != null)
			cateOutPut.setImage(photoConverter.toLinkCategory(cateOutPut.getImage()));

		List<CategoryOutPut> list = new ArrayList<CategoryOutPut>();
		CategoryOutPut cate = modelMapper.map(s.getCategory(), CategoryOutPut.class);
		if (cate.getImage() != null)
			cate.setImage(photoConverter.toLinkCategory(cate.getImage()));
		list.add(cate);
		cateOutPut.setSubCategories(list);

		product.setCategory(cateOutPut);
		product.setCategoryId(s.getCategory().getId());
		product.setShopId(s.getShop().getId());
		product.setShopName(s.getShop().getName());
		if (s.getShop().getAvatar() != null)
			product.setShopAvatar(photoConverter.toLinkAvatarShop(s.getShop().getAvatar()));
		try {
			// product.setRating(futureRating.get());
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
