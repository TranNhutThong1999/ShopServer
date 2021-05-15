package kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kltn.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findOneByPhone(String phone);
	Optional<User> findOneById(int id);
	Optional<User> findOneByOtp(String otp);
	
}
 