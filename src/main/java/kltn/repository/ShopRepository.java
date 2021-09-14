package kltn.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, String>{
	Optional<Shop> findOneById(String id);
	Optional<Shop> findOneByEmail(String email);
	Optional<Shop> findOneByIdAndOtp(String shopId, String otp);
	Optional<Shop> findOneByEmailAndOtp(String email, String otp);
	
	@Query(value = " select date(orders.created_date) as date, sum(orders.total_money) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and month(orders.created_date) = :month and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
	List<Map<String, Object>> getMonth(String shopId, int year, int month, int status);
	
	@Query(value = " select date(orders.created_date) as date, sum(orders.total_money) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
	List<Map<String, Object>> getYear(String shopId, int year, int status);
	
	@Query(value = " select date(orders.created_date) as date, count(orders.id) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and month(orders.created_date) = :month and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
	List<Map<String, Object>> getTotalMonth(String shopId, int year, int month, int status);
	
	@Query(value = " select date(orders.created_date) as date, count(orders.total_money) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
	List<Map<String, Object>> getTotalYear(String shopId, int year, int status);
}
