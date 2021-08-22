package kltn.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
public class NotificationDTO {
	private String id;
	private Date createDate;
	private int type;
	private String avatar;
	private String title;
	private String subTitle;
	private String time;
	private int productId;
	private String orderCode;
	private int commentId;
	private int status;
	private int orderId;
	private int isSeen;
	
}

