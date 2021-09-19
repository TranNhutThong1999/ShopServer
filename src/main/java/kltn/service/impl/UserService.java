package kltn.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kltn.converter.UserConverter;
import kltn.dto.UserDTO;
import kltn.entity.User;
import kltn.repository.UserRepository;
import kltn.service.IUserService;

@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final UserConverter userConverter;

	@Autowired
	public UserService(UserRepository userRepository, UserConverter userConverter) {
		super();
		this.userRepository = userRepository;
		this.userConverter = userConverter;
	}

	@Override
	public Optional<User> findOneByPhone(String phone) {
		// TODO Auto-generated method stub
		return userRepository.findOneByPhone(phone);
	}

	@Override
	public UserDTO findByPhone(String phone) throws Exception {
		// TODO Auto-generated method stub
		return userConverter
				.toDTO(userRepository.findOneByPhone(phone).orElseThrow(() -> new Exception("Phone was not found")));
	}

	@Override
	public Optional<User> findOneById(String id) {
		// TODO Auto-generated method stub
		return userRepository.findOneById(id);
	}

	@Override
	public String findOtp(String otp) throws Exception {
		// TODO Auto-generated method stub
		User u = userRepository.findOneByOtp(otp).orElseThrow(() -> new Exception("otp was not found"));
		return u.getId();
	}

	@Override
	public UserDTO findById(String id) {
		// TODO Auto-generated method stub
		return userConverter.toDTO(userRepository.findOneById(id).get());
	}

}
