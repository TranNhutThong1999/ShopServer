package kltn.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.security.core.Authentication;

import kltn.entity.Address;
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
			noti.setSubTitle("Đơn hàng #" + noti.getOrderCode() + ", đang chờ được tiếp nhận");
		} else if (noti.getStatus() == Common.ORDER_TRANSPORT) {
			noti.setTitle("Thông tin đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrderCode() +", đã sẵn sàng giao đến quý khách");
		} else if (noti.getStatus() == Common.ORDER_SUCCESS) {
			noti.setTitle("Thông báo tình trạng đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrderCode() +", đã giao đến quý khách");
		}else if(noti.getStatus() == Common.ORDER_CANCEL) {
			noti.setTitle("Thông báo tình trạng đơn hàng");
			noti.setSubTitle("Đơn hàng #"+ noti.getOrderCode() +", đã bị huỷ");
		}
		return noti;
	}

	public static String parse(Date date) {
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
}
