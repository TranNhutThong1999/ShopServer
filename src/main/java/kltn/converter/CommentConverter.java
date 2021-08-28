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
import kltn.entity.Comment;
import kltn.entity.Reply;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.repository.ProductRepository;
import kltn.repository.UserRepository;

@Component
public class CommentConverter implements IConverter<Comment, CommentOuput> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PhotoConverter photoConverter;
	
	@Override
	public Comment toEntity(CommentOuput d) {
		return modelMapper.map(d, Comment.class);
	}
	
	public CommentOuput toReply(Reply d) {
		CommentOuput outPut = new CommentOuput();
		outPut.setId(d.getId());
		outPut.setContent(d.getContent());
		outPut.setParentId(d.getComment().getId());
		outPut.setCreateOn(d.getCreatedDate());
		if (d.getUser() != null) {
			User u = d.getUser();
			outPut.setUserId(u.getId());
			outPut.setFullName(u.getFirstName() + " " + u.getLastName());
			outPut.setUserAvatar(photoConverter.toLinkAvatarUser(u.getPictureURL()));
		} else {
			Shop s = d.getShop();
			outPut.setShopId(s.getId());
			outPut.setFullName(s.getName());
			outPut.setUserAvatar(photoConverter.toLinkAvatarShop(s.getAvatar()));
		}
		return outPut;
	}

	@Override
	public CommentOuput toDTO(Comment d) {
		CommentOuput outPut = new CommentOuput();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		if(d.getReplies()!=null) {
			CompletableFuture<List<CommentOuput>> futureChild = CompletableFuture.supplyAsync(()->d.getReplies().stream().map((e) -> toReply(e)).collect(Collectors.toList()), executor);
			try {
				outPut.setCommentChild(futureChild.get());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			executor.shutdown();
		}
		
		
		outPut.setId(d.getId());
		outPut.setContent(d.getContent());
		outPut.setCreateOn(d.getCreatedDate());
		outPut.setProductId(d.getProduct().getId());
		if (d.getUser() != null) {
			User u = d.getUser();
			outPut.setUserId(u.getId());
			outPut.setFullName(u.getFirstName() + " " + u.getLastName());
			outPut.setUserAvatar(photoConverter.toLinkAvatarUser(u.getPictureURL()));
		} else {
			Shop s = d.getShop();
			outPut.setShopId(s.getId());
			outPut.setFullName(s.getName());
			outPut.setUserAvatar(photoConverter.toLinkAvatarShop(s.getAvatar()));
		}
		
		return outPut;
	}

}
