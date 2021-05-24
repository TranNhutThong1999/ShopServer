package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Banner;
import kltn.entity.District;
import kltn.entity.Provincial;

public interface DistrictRepository extends JpaRepository<District, Integer>{
	Optional<District> findOneByCode(int code);
}
