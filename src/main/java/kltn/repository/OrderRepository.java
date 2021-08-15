package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kltn.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findByShop_Id(int userId);
	Optional<Order> findOneByIdAndShop_Id(int id, int userId);
//	Optional<List<Order>> findOneByShop_Id(int id);
}
