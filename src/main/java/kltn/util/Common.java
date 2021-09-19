package kltn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kltn.entity.Address;
import kltn.entity.Country;
import kltn.entity.Notification;
import kltn.security.MyShop;

public class Common {
	public static final int ORDER_PROCESS = 1;
	public static final int ORDER_TRANSPORT = 2;
	public static final int ORDER_SUCCESS = 3;
	public static final int ORDER_CANCEL = 4;

	public static final int ACTION_LIKE = 1;
	public static final int ACTION_BOUGHT = 2;
	
	public static final int NOTI_ORDER = 0;
	public static final int NOTI_CMT = 1;
	
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
	

	public static Notification setDataOrder(Notification noti) {
		if (noti.getStatus() == Common.ORDER_PROCESS) {
			noti.setTitle("Thông tin đơn hàng");
			noti.setSubTitle("Đơn hàng #" + noti.getOrder().getOrderCode() + ", đang chờ được tiếp nhận");
		} else if (noti.getStatus() == Common.ORDER_TRANSPORT) {
			noti.setTitle("Thông tin đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrder().getOrderCode() +", đã sẵn sàng giao đến quý khách");
		} else if (noti.getStatus() == Common.ORDER_SUCCESS) {
			noti.setTitle("Thông báo tình trạng đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrder().getOrderCode() +", đã giao đến quý khách");
		}else if(noti.getStatus() == Common.ORDER_CANCEL) {
			noti.setTitle("Thông báo tình trạng đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrder().getOrderCode() +", đã bị huỷ");
		}
		return noti;
	}

	public static List<Country> parseFileCountry() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Country>> typeReference = new TypeReference<List<Country>>(){};
		File file =null;
		try {
			file = ResourceUtils.getFile("classpath:country.json");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream inputStream = TypeReference.class.getClassLoader().getResourceAsStream("country.json");
		try {
			List<Country> c = mapper.readValue(inputStream,typeReference);
			return c;
		} catch (IOException e){
			System.out.println("Unable to save users: " + e.getMessage());
		}
	return null;
	
}
	public static String parse(Date date) {
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
}
