package kltn.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "device_token")
public class DeviceToken {
	
	@Id
	private String FCMToken;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "shop_id")
	private Shop shop; 
	
	private Date createDate;
}
