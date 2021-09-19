package kltn.service;

import java.util.Optional;

import kltn.dto.UserDTO;
import kltn.entity.User;

public interface IUserService {
	Optional<User> findOneByPhone(String phone);

	UserDTO findByPhone(String phone) throws Exception;

	Optional<User> findOneById(String id);
	
	UserDTO findById(String id);
	
	String findOtp(String otp) throws Exception;
	
}
