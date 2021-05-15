package kltn.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kltn.converter.UserConverter;
import kltn.dto.UserDTO;
import kltn.entity.User;
import kltn.exception.UserWasNotFoundException;
import kltn.repository.RoleRepository;
import kltn.repository.UserRepository;
import kltn.service.IUserService;
import kltn.util.SMSSender;

@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final UserConverter userConverter;
	private final RoleRepository roleRepository;
	private final SMSSender sms;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder encoder, UserConverter userConverter,
			RoleRepository roleRepository, SMSSender sms) {
		super();
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.userConverter = userConverter;
		this.roleRepository = roleRepository;
		this.sms = sms;
	}

	@Override
	public Optional<User> findOneByPhone(String phone) {
		// TODO Auto-generated method stub
		return userRepository.findOneByPhone(phone);
	}

	@Override
	public UserDTO save(UserDTO u) {
		// TODO Auto-generated method stub
		User user = userConverter.toEntity(u);
		user.setEnabled(false);
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole(roleRepository.findOneByName("ROLE_USER"));
		//generate token
		String otp = user.generateToken();
		//send sms
		sms.excute(user.getPhone(), otp);
		return userConverter.toDTO(userRepository.save(user));
	}

	@Override
	public boolean verifyOTP(String otp, String phone) {
		// TODO Auto-generated method stub
		User user = userRepository.findOneByPhone(phone)
				.orElseThrow(() -> new UserWasNotFoundException("Phone was not found"));
		if (user.checkOTP(otp)) {
			user.setOtp("");
			user.setEnabled(true);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public UserDTO findByPhone(String phone) throws Exception {
		// TODO Auto-generated method stub
		return userConverter
				.toDTO(userRepository.findOneByPhone(phone).orElseThrow(() -> new Exception("Phone was not found")));
	}

	@Override
	public void sendOtp(String phone) throws Exception {
		// TODO Auto-generated method stub
		User user = userRepository.findOneByPhone(phone).orElseThrow(() -> new Exception("phone was not found"));
		String otp = user.generateToken();
		userRepository.save(user);
		sms.excute(phone, otp);
	}

	@Override
	public Optional<User> findOneById(int id) {
		// TODO Auto-generated method stub
		return userRepository.findOneById(id);
	}

	@Override
	public int findOtp(String otp) throws Exception {
		// TODO Auto-generated method stub
		User u = userRepository.findOneByOtp(otp).orElseThrow(() -> new Exception("otp was not found"));
		return u.getId();
	}

	@Override
	public void changePassword(int id, String password) throws Exception {
		// TODO Auto-generated method stub
		User u = userRepository.findOneById(id).orElseThrow(()-> new Exception("UserId doesn't exist"));
		u.setPassword(encoder.encode(password));
		userRepository.save(u);
	}

	@Override
	public UserDTO findById(int id){
		// TODO Auto-generated method stub
		return userConverter.toDTO(userRepository.findOneById(id).get());
	}

}
