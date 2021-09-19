package kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import kltn.entity.DeviceToken;
import kltn.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	Page<Notification> findByShop_Id(String userId, Pageable page);
	Optional<Notification> findOneByIdAndShop_Id(String id, String shopId);
}
