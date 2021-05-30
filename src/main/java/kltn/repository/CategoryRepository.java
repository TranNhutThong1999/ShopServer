package kltn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	List<Category> findByCategoryIsNull();
}
