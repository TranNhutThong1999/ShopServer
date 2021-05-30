package kltn.service;


import org.springframework.data.domain.Page;

import kltn.api.input.CommentOuput;

public interface ICommentService {
	Page<CommentOuput> findByProductId(int id, int pageSize, int pageNumber);
	void save(CommentOuput m, String userName) throws Exception;
}
