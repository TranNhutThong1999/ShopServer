package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Banner;
import kltn.entity.Provincial;

public interface ProvincialRepository extends JpaRepository<Provincial, Integer>{
	Optional<Provincial> findOneByCode(int code);
	
}
