package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import kltn.entity.DeviceToken;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String>{
	Optional<DeviceToken> findOneByUser_Id(int userId);
}
