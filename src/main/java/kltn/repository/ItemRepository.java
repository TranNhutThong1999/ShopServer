package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
}
