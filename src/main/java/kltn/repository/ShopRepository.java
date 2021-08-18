package kltn.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, String>{
	Optional<Shop> findOneByUserName(String userName);
	Optional<Shop> findOneById(String id);
	Optional<Shop> findOneByEmail(String email);
	Optional<Shop> findOneByEmailAndOtp(String email, String otp);
	Optional<Shop> findOneByIdAndOtp(String shopId, String otp);
}
