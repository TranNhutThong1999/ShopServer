package kltn.service;

import java.util.List;

import org.springframework.data.domain.Page;

import kltn.dto.CommentDTO;

public interface ICommentService {
	Page<CommentDTO> findByProductId(int id, int pageSize, int pageNumber);
}
