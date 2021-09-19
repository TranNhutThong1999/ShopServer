package kltn.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO extends AbstractDTO{
	private String orderCode;
	private String orderTime;
	private String deliveryTime;
	
	private String infor;
	private String address;
	private List<ItemDTO> product;
	
	private String transportedName;
	private String paymentName;
	
	private int status;
	private int isPaymentOnline;
	private int isPayment;
	
	private double tempPrice;
	private double feeShip;
	private double TotalMoney;
	
	
}
