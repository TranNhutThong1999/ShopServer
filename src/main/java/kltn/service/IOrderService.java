package kltn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import kltn.api.output.ListOrder;
import kltn.dto.OrderDTO;
import kltn.entity.Order;

public interface IOrderService {
//	void save(OrderDTO order, Authentication auth) throws Exception;
	List<ListOrder> getListOrder(Authentication auth);
	
	OrderDTO getOrder(int id, Authentication auth) throws Exception;
	void updateCancelStatus(int orderId, String reason, Authentication auth) throws Exception;
	void updateStatus(int orderId, Authentication auth, int status) throws Exception;
	void updateStatusSuccess(int orderId, String shopId, int status);
}
