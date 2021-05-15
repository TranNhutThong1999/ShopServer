package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.RepliesDTO;
import kltn.entity.Replies;
import kltn.repository.CommentRepository;
import kltn.repository.UserRepository;
import kltn.security.CustomUserDetail;

@Component
public class RepliesConverter implements IConverter<Replies, RepliesDTO> {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private CustomUserDetail customUserDetail;
	
	@Override
	public Replies toEntity(RepliesDTO d) {
		// TODO Auto-generated method stub
		Replies r = modelMapper.map(d, Replies.class);
		r.setComment(commentRepository.findById(d.getCommentId()).get());
		r.setUser(userRepository.findById(customUserDetail.getPrincipleId()).get());
		return r;
	}

	@Override
	public RepliesDTO toDTO(Replies s) {
		// TODO Auto-generated method stub
		RepliesDTO r = modelMapper.map(s, RepliesDTO.class);
		r.setCommentId(s.getComment().getId());
		r.setUserId(s.getUser().getId());
		return r;
	}

}
