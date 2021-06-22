package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Banner;
import kltn.entity.Province;

public interface ProvincialRepository extends JpaRepository<Province, Integer>{
	Optional<Province> findOneByCode(int code);
	
}
