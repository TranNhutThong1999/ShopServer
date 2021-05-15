package kltn.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.CommentDTO;
import kltn.entity.Comment;
import kltn.repository.ProductRepository;
import kltn.repository.UserRepository;
import kltn.security.CustomUserDetail;

@Component
public class CommentConverter implements IConverter<Comment, CommentDTO> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RepliesConverter repliesConverter;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomUserDetail customUserDetail;
	
	@Override
	public Comment toEntity(CommentDTO d) {
		// TODO Auto-generated method stub
		Comment m = modelMapper.map(d, Comment.class);
		m.setUser(userRepository.findById(customUserDetail.getPrincipleId()).get());
		m.setProduct(productRepository.findById(d.getProductId()).get());
		return m;
	}

	@Override
	public CommentDTO toDTO(Comment s) {
		// TODO Auto-generated method stub
		CommentDTO m = modelMapper.map(s, CommentDTO.class);
		m.setProductId(s.getProduct().getId());
		m.setReplies(s.getReplies().stream().map(repliesConverter::toDTO).collect(Collectors.toList()));
		m.setUserId(s.getUser().getId());
		return m;
	}

}
