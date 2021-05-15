package kltn.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.dto.UserDTO;
import kltn.entity.User;
import lombok.extern.apachecommons.CommonsLog;

@Component
public class UserConverter implements IConverter<User, UserDTO>{
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public User toEntity(UserDTO d) {
		// TODO Auto-generated method stub
		return modelMapper.map(d, User.class);
	}

	@Override
	public UserDTO toDTO(User s) {
		// TODO Auto-generated method stub
		UserDTO user =modelMapper.map(s, UserDTO.class);
		user.setRole(s.getRole().getName());
		user.setFullName(s.getFirstName() + " " + s.getLastName());
		return user;
	}
}
