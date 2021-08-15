package kltn.converter;

import java.io.File;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kltn.api.output.ListOrder;
import kltn.dto.OrderDTO;
import kltn.dto.ItemDTO;
import kltn.entity.Item;
import kltn.entity.Order;
import kltn.util.Constants;

@Component
public class OrderConverter implements IConverter<Order, OrderDTO>{
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Constants constant;
	
	@Autowired
	private ItemConverter itemConverter;
	
	@Override
	public Order toEntity(OrderDTO d) {
		// TODO Auto-generated method stub
		Order order = modelMapper.map(d,  Order.class);
		return order;
	}

	public ListOrder tolist(Item s, Order or) {
		ListOrder list = new ListOrder();
		list.setPrice(s.getPrice());
		list.setName(s.getProduct().getName());
		list.setPhoto(constant.showImage + File.separator+ "images" + File.separator + s.getProduct().getPhotos().get(0).getName());
		list.setProductId(s.getProduct().getId());
		list.setQuantity(s.getQuantity());
		list.setOrderId(or.getId());
		list.setStatus(or.getStatus());
		return list;
	}
	@Override
	public OrderDTO toDTO(Order s) {
		// TODO Auto-generated method stub
		OrderDTO d = modelMapper.map(s, OrderDTO.class);
		d.setIsPayment(s.getPayment().isPayment() == true ? 1 : 0);
		d.setIsPaymentOnline(s.getPayment().isPamentOnline() == true ? 1 : 0);
		d.setPaymentName(s.getPayment().getPaymentName());
		d.setProduct(s.getDetail().stream().map(itemConverter::toDTO).collect(Collectors.toList()));
		d.setIsPaymentOnline(s.getPayment().isPamentOnline() == true ? 1 : 0);
		d.setPaymentName(s.getPayment().getPaymentName());
		d.setIsPayment(s.getPayment().isPayment() == true ? 1 : 0);
		return d;
	}
	
	
	
	
}
