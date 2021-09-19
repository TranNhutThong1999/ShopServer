package kltn.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Order;
import kltn.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, String>{
	Optional<Shop> findOneById(String id);
	Optional<Shop> findOneByEmail(String email);
	Optional<Shop> findOneByIdAndOtp(String shopId, String otp);
	Optional<Shop> findOneByEmailAndOtp(String email, String otp);
	
	@Query(value = " select DATE_FORMAT(orders.created_date, '%d/%m/%Y') as date, sum(orders.total_money) as money, count( distinct orders.id) as total, sum(item.quantity) as quantity from orders, item where orders.id = item.order_id and orders.shop_id = :shopId and year(orders.created_date) = :year and month(orders.created_date) = :month and orders.status = :status group by DATE_FORMAT(orders.created_date, '%d/%m/%Y'), orders.shop_id order by DATE_FORMAT(orders.created_date, '%d/%m/%Y')", nativeQuery = true)
	List<Map<String, Object>> getMonth(String shopId, int year, int month, int status);
	
	@Query(value = " select DATE_FORMAT(orders.created_date, '%m/%Y') as date, sum(orders.total_money) as money, count( distinct orders.id) as total, sum(item.quantity) as quantity from orders, item where orders.id = item.order_id and orders.shop_id = :shopId and year(orders.created_date) = :year and orders.status = :status group by month(orders.created_date), DATE_FORMAT(orders.created_date, '%m/%Y'), orders.shop_id order by month(orders.created_date)", nativeQuery = true)
	List<Map<String, Object>> getYear(String shopId, int year, int status);
	
//	@Query(value = " select date(orders.created_date) as date, count(orders.id) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and month(orders.created_date) = :month and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
//	List<Map<String, Object>> getTotalMonth(String shopId, int year, int month, int status);
//	
//	@Query(value = " select date(orders.created_date) as date, count(orders.id) as money from orders where orders.shop_id = :shopId and year(orders.created_date) = :year and orders.status = :status group by date(orders.created_date), orders.shop_id", nativeQuery = true)
//	List<Map<String, Object>> getTotalYear(String shopId, int year, int status);
}
