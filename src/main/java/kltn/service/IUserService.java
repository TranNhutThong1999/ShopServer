package kltn.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kltn.dto.UserDTO;
import kltn.entity.User;

@Service
public interface IUserService {
	Optional<User> findOneByPhone(String phone);

	UserDTO findByPhone(String phone) throws Exception;

	Optional<User> findOneById(int id);
	
	UserDTO findById(int id);
	
	int findOtp(String otp) throws Exception;
	
}
