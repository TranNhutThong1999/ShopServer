package kltn.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kltn.dto.UserDTO;
import kltn.entity.User;

@Service
public interface IUserService {
	Optional<User> findOneByPhone(String phone);

	UserDTO findByPhone(String phone) throws Exception;

	UserDTO save(UserDTO u);

	boolean verifyOTP(String otp, String phone);

	void sendOtp(String phone) throws Exception;

	Optional<User> findOneById(int id);
	
	UserDTO findById(int id);
	
	int findOtp(String otp) throws Exception;
	
	void changePassword(int id,String password) throws Exception;
}
