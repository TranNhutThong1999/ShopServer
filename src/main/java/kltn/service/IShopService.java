package kltn.service;

import org.springframework.security.core.Authentication;

import kltn.api.input.ShopDetail;
import kltn.dto.AddressDTO;
import kltn.dto.ShopDTO;

public interface IShopService {
	ShopDTO save(ShopDTO s);

	ShopDTO getOne(Authentication auth);

	ShopDTO findOneByUserName(String userName);

	void delete(Authentication auth);

	void update(ShopDTO shop, Authentication auth);
	
	void sendOTP(String mail) throws Exception;

	int checkOTP(String otp, String email) throws Exception;

	void verify(String otp, String email) throws Exception;

	void changePassword(String password, Authentication auth);
	
	void updatePassword(int shopId, String password, String otp) throws Exception;
	
	ShopDTO createDetail(ShopDetail s) throws Exception;
	
	AddressDTO getAddress(Authentication auth);
}
