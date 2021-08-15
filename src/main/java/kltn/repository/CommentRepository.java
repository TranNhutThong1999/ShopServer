package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findAllByProduct_Id(int productId);

	Optional<Comment> findOneByIdAndUser_Id(int id, int userId);

	@Query(value = "select * from comment where limit :limit", nativeQuery = true)
	List<Comment> findByShopIdAndLimit();
}
