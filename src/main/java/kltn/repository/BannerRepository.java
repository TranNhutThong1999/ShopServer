package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Integer>{
	@Query(value = "select * from banner order by rand() limit 1",nativeQuery = true)
	Banner findOneRandom();
}
