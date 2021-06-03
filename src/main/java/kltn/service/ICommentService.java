package kltn.service;


import org.springframework.data.domain.Page;

import kltn.api.output.CommentOuput;


public interface ICommentService {
	Page<CommentOuput> findByProductId(int id, int pageSize, int pageNumber);
	CommentOuput save(CommentOuput m, String userName) throws Exception;
}
