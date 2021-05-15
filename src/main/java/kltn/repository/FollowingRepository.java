package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kltn.entity.Following;

public interface FollowingRepository extends JpaRepository<Following, Integer>{
	
	@Query(value = "select count(*) as a from following where following.shop_id = :shopId",nativeQuery = true)
	int countUserFollow(int shopId);
	
	Optional<Following> findOneByUser_IdAndShop_Id(int userId, int shopId);
}
