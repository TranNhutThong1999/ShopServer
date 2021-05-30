package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.api.input.CommentOuput;
import kltn.converter.CommentConverter;
import kltn.dto.CommentDTO;
import kltn.entity.Comment;
import kltn.repository.CommentRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.service.ICommentService;

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
	
	@Override
	public Page<CommentOuput> findByProductId(int id, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return commentRepository.findAllByProduct_IdAndCommentIdIsNull(id, pageable).map(commentConverter::toDTO);
	}
	@Override
	public void save(CommentOuput m, String userName) throws Exception {
		// TODO Auto-generated method stub
		Comment cm = commentConverter.toEntity(m);
		if(m.getParentId()!=0) {
			cm.setComment(commentRepository.findById(m.getParentId()).orElseThrow(()-> new Exception("commentId was not found") ));
		}
		cm.setShop(ShopRepository.findOneByUserName(userName).get());
		cm.setProduct(productRepository.findOneById(m.getProductId()).orElseThrow(()-> new Exception("productId was not found")));
		commentRepository.save(cm);
	}
}
