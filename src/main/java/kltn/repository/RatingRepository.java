package kltn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
	Page<Rating> findAllByProduct_Id(int productId, Pageable p);

	@Query(value = "select count(*) from rating where rating.product_id = :productId", nativeQuery = true)
	double quantityStar(int productId);

	@Query(value = "select count(rating.star) from rating where rating.product_id = :productId", nativeQuery = true)
	double sumStar(int productId);
}
