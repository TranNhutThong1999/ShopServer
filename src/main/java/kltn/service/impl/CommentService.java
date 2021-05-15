package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.converter.CommentConverter;
import kltn.dto.CommentDTO;
import kltn.repository.CommentRepository;
import kltn.service.ICommentService;

@Service
public class CommentService implements ICommentService{
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentConverter commentConverter;
	
	@Override
	public Page<CommentDTO> findByProductId(int id, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return commentRepository.findAllByProduct_Id(id, pageable).map(commentConverter::toDTO);
	}

}
