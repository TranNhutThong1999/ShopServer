package kltn.service;


import java.util.List;

import org.springframework.security.core.Authentication;

import kltn.api.output.CommentOuput;


public interface ICommentService {
	CommentOuput save(CommentOuput m, Authentication auth) throws Exception;
	void delete(Authentication auth, int id);
	List<CommentOuput> findByShopAndLimit(Authentication auth, int limit);
}
