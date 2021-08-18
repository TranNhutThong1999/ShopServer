package kltn.util;

import java.util.Random;

import org.springframework.security.core.Authentication;

import kltn.entity.Address;
import kltn.security.MyShop;

public class Common {
	public static final int ORDER_PROCESS = 1;
	public static final int ORDER_TRANSPORT = 2;
	public static final int ORDER_SUCCESS = 3;
	public static final int ORDER_CANCEL = 4;

	public static final int ACTION_LIKE = 1;
	public static final int ACTION_BOUGHT = 2;
	
	public static String convertToString(Address address) {
		StringBuilder s = new StringBuilder();
		s.append(address.getLocation());
		s.append(", ");
		s.append(address.getWards());
		s.append(", ");
		s.append(address.getDistrict());
		s.append(", ");
		s.append(address.getProvince());
		return s.toString();
	}

	public static String getIdFromAuth(Authentication auth) {
		MyShop u = (MyShop) auth.getPrincipal();
		return u.getId();
	}

	public static String random(int quantity) {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%0" + quantity + "d", number);
	}

}
