package kltn.service.impl;


import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.output.CommentOuput;
import kltn.converter.CommentConverter;
import kltn.entity.Comment;
import kltn.entity.Product;
import kltn.entity.Reply;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.repository.CommentRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ReplyRepository;
import kltn.repository.ShopRepository;
import kltn.service.ICommentService;
import kltn.util.Common;

@Service
public class CommentService implements ICommentService{
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentConverter commentConverter;
	
	@Autowired
	private ShopRepository ShopRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMap;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Override
	public CommentOuput save(CommentOuput m, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Optional<Product> pro = productRepository.findOneByIdAndShop_Id(m.getProductId(), m.getUserId());
		if(pro.isPresent()) {
			Shop u = ShopRepository.findOneById(Common.getIdFromAuth(auth)).get();
			Comment cm = null;
			if (m.getParentId() == null) {
				cm = commentConverter.toEntity(m);
				cm.setShop(u);
				cm.setProduct(productRepository.findOneById(m.getProductId())
						.orElseThrow(() -> new Exception("productId was not found")));
				return commentConverter.toDTO(commentRepository.save(cm));
			} else {
				cm = commentRepository.findById(m.getParentId())
						.orElseThrow(() -> new Exception("commentId was not found"));
				Reply rl = modelMap.map(m, Reply.class);
				rl.setComment(cm);
				rl.setShop(u);
				return commentConverter.toReply(replyRepository.save(rl));
			}
		}
		 throw new Exception("auth");
	}

	@Override
	public void delete(Authentication auth, int id) {
		// TODO Auto-generated method stub
		Optional<Comment> comment = commentRepository.findOneByIdAndUser_Id(id, Common.getIdFromAuth(auth));
		if(comment.isPresent()) {
			commentRepository.delete(comment.get());
		}
	}

	@Override
	public List<CommentOuput> findByShopAndLimit(Authentication auth, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<CommentOuput> findByShopAndLimit(Authentication auth, int limit) {
//		// TODO Auto-generated method stub
//		commentRepository.find
//		return null;
//	}
}
