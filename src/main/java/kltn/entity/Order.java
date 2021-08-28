package kltn.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "order")
public class Order extends Abstract {
	private String orderCode;
	private String orderTime;
	private String deliveryTime;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;
	
	private String infor;
	private String address;
	private int status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<Item> detail;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	private String transportedName;
	private float tempPrice;
	private float feeShip;
	private float totalMoney;
	
}
