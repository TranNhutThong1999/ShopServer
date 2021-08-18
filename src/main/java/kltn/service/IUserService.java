package kltn.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kltn.dto.UserDTO;
import kltn.entity.User;

@Service
public interface IUserService {
	Optional<User> findOneByPhone(String phone);

	UserDTO findByPhone(String phone) throws Exception;

	Optional<User> findOneById(String id);
	
	UserDTO findById(String id);
	
	String findOtp(String otp) throws Exception;
	
}
