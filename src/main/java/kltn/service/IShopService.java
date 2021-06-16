package kltn.service;

import kltn.dto.ShopDTO;

public interface IShopService {
	ShopDTO Save(ShopDTO s);

	ShopDTO findOneById(int id) throws Exception;

	ShopDTO findOneByUserName(String userName);

	void delete(String userName);

	void sendOTP(String mail) throws Exception;

	int checkOTP(String otp, String email) throws Exception;

	void verify(String otp, String email) throws Exception;

	void changePassword(String password, String userName);
}
