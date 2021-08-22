package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import kltn.entity.DeviceToken;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String>{
	Optional<DeviceToken> findOneByFCMTokenAndShop_Id(String token, String shopId);
	Optional<DeviceToken> findOneByUser_Id(String userId);
	Optional<DeviceToken> findOneByShop_Id(String shopId);
	
}
