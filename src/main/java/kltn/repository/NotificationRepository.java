package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import kltn.entity.DeviceToken;
import kltn.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	List<Notification> findByShop_Id(String userId, Sort sort);
	Optional<Notification> findOneByIdAndShop_Id(int id, String shopId);
}
