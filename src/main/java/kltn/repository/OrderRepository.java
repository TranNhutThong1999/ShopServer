package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kltn.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	List<Order> findByShop_Id(String userId, Sort sort);
	Optional<Order> findOneByIdAndShop_Id(int id, String shopId);
}
