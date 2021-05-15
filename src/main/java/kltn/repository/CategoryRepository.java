package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
