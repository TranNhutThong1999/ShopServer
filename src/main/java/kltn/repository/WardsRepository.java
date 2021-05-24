package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Banner;
import kltn.entity.District;
import kltn.entity.Wards;

public interface WardsRepository extends JpaRepository<Wards, Integer>{
	Optional<Wards> findOneByCode(int code);
}
