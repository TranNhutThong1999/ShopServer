package kltn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	Page<Comment> findAllByProduct_Id(int productId, Pageable p);
}
