package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findAllByProduct_Id(int productId);
	List<Comment> findAllByProduct_IdAndProduct_Shop_Id(int productId, String shopId);

	Optional<Comment> findOneByIdAndShop_Id(int id, String shopId);

	@Query(value = "select * from comment where limit :limit", nativeQuery = true)
	List<Comment> findByShopIdAndLimit();
}
