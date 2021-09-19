package kltn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import kltn.api.input.ShopDetail;
import kltn.dto.AddressDTO;
import kltn.dto.NotificationDTO;
import kltn.dto.ShopDTO;
import kltn.dto.StatisticalDTO;

public interface IShopService {
	ShopDTO save(ShopDTO s);

	ShopDTO getOne(Authentication auth);

	void delete(Authentication auth);

	ShopDTO update(ShopDTO shop, Authentication auth);

	String sendOTP(String mail) throws Exception;

	String checkOTP(String otp, String email) throws Exception;

	void verify(String otp, String shopId) throws Exception;

	void changePassword(String password, Authentication auth);

	void updatePassword(String shopId, String password, String otp) throws Exception;

	ShopDTO createDetail(ShopDetail s) throws Exception;

	AddressDTO getAddress(Authentication auth);

	void saveFCMToken(String token, Authentication auth);

	Page<NotificationDTO> getListNoti(Authentication auth, int pageSize, int pageNumber);

	void setIsSeen(Authentication auth, String id);

	StatisticalDTO getDataMonth(String type, String time, Authentication auth);

	void exportExcel(String time, Authentication auth);
}
