package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kltn.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findByShop_Id(String userId, Sort sort);
	Optional<Order> findOneByIdAndShop_Id(int id, String shopId);
	
	@Query(value = "select * from orders where orders.shop_id = 'S002' and year(orders.created_date) = 2021 and month(orders.created_date) = 4  and orders.status = 3", nativeQuery = true)
	List<Order> getOrder();
}
