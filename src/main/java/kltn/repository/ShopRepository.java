package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer>{
	Optional<Shop> findOneByUserName(String userName);
	Optional<Shop> findOneById(int id);
	Optional<Shop> findOneByEmail(String email);
	Optional<Shop> findOneByEmailAndOtp(String email, String otp);
	Optional<Shop> findOneByIdAndOtp(int shopId, String otp);
}
