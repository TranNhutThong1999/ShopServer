package kltn.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class Payment extends Abstract{
	private boolean isPamentOnline;
	private boolean isPayment;
	private String paymentName;
	
}
