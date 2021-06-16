package kltn.annotation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import kltn.entity.Shop;
import kltn.entity.User;
import kltn.repository.ShopRepository;
import kltn.service.IShopService;
import kltn.service.IUserService;


public class EmailExistConstraintValidator implements ConstraintValidator<EmailExist, String> {
	@Autowired
	private ShopRepository shopRepository;

	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if (value != null) {
			Optional<Shop> s = shopRepository.findOneByEmail(value);
			return s.isPresent() ? false : true;
		}
		return true;
	}

}
