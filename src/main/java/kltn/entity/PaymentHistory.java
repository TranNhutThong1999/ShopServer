package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "payment_history")
public class PaymentHistory extends Abstract{
	
}
