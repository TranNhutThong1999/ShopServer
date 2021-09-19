package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	List<Item> findByProduct_Id(int productId);
}
