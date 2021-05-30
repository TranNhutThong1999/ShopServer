package kltn.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.api.input.CommentOuput;
import kltn.entity.Comment;
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

	@Override
	public Comment toEntity(CommentOuput d) {
		return modelMapper.map(d, Comment.class);
	}

	@Override
	public CommentOuput toDTO(Comment d) {
		CommentOuput outPut = new CommentOuput();
		outPut.setId(d.getId());
		outPut.setContent(d.getContent());
		outPut.setCreateOn(d.getCreatedDate());
		outPut.setProductId(d.getProduct().getId());
		if (d.getUser() != null) {
			User u = d.getUser();
			outPut.setUserId(u.getId());
			outPut.setFullName(u.getFirstName() + " " + u.getLastName());
			outPut.setUserAvatar(u.getPictureURL());
		} else {
			Shop s = d.getShop();
			outPut.setUserId(s.getId());
			outPut.setFullName(s.getNameShop());
			outPut.setUserAvatar(s.getAvatar());
		}
		if (d.getComment() != null)
			outPut.setParentId(d.getComment().getId());
		outPut.setCommentChild(d.getReplies().stream().map((e) -> toDTO(e)).collect(Collectors.toList()));
		return outPut;
	}

}
