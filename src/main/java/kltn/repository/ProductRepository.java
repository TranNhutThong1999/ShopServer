package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Comment;
import kltn.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	Optional<Product> findOneById(int id);
	
	Page<Product> findByShop_id(String shopId, Pageable p);
	
	//@Query(value = "select product.* from product, sub_category where product.sub_category_id = sub_category.id and sub_category.id = :subCateroryId limit :quantity",nativeQuery = true)
	Page<Product> findByCategory_Id(Pageable p, int cateroryId);
	
	@Query(value = "select * from product order by rand() limit :limit",nativeQuery = true)
	List<Product> findRandomLimit(int limit);
	
	Page<Product> findByShop_Id(int id, Pageable p);
	Optional<Product> findOneByIdAndShop_Id(int id, String shopId);
	
	Page<Product> findByCategory_Category_Id(Pageable p, int cateroryId);
}
