package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer>{
	List<Photo> findByProduct_Shop_id(int id);
	Optional<Photo> findOneByProduct_id(int productId);
}
