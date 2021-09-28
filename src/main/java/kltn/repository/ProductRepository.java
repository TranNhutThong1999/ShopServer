package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Comment;
import kltn.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	Optional<Product> findOneByIdAndIsDeleted(int id, boolean isDeleted);

	Page<Product> findByShop_IdAndIsDeleted(String shopId, boolean isDeleted, Pageable p);

	// @Query(value = "select product.* from product, sub_category where
	// product.sub_category_id = sub_category.id and sub_category.id =
	// :subCateroryId limit :quantity",nativeQuery = true)
	Page<Product> findByCategory_IdAndIsDeleted(Pageable p, int cateroryId, boolean isDeleted);

	@Query(value = "select * from product where is_deleted = false order by rand() limit :limit", nativeQuery = true)
	List<Product> findRandomLimit(int limit);

	Optional<Product> findOneByIdAndShop_IdAndIsDeleted(int id, String shopId, boolean isDeleted);

	Page<Product> findByCategory_Category_IdAndIsDeleted(Pageable p, int cateroryId, boolean isDeleted);
	
	List<Product> findByShop_IdAndIsDeleted(String shopId, boolean isDeleted);
}
