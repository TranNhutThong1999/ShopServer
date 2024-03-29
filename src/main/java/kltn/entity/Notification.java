package kltn.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
@Data
@Entity
@Table(name = "notification")
public class Notification {
	 @Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "id", columnDefinition = "VARCHAR(255)")
	private String id;
	private int type;
	private String avatar;
	private Date createDate;
	private String title;
	private String subTitle;
	private String time;
	private int status;
	private int isSeen;
	
	
	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;
	
	@ManyToOne
	@JoinColumn(name = "reply_id")
	private Reply reply;
	
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="shop_id")
	private Shop shop;

	public Notification() {
		super();
		this.createDate = new Date();
		this.isSeen = 0;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", type=" + type + ", avatar=" + avatar + ", createDate=" + createDate
				+ ", title=" + title + ", subTitle=" + subTitle + ", time=" + time + ", status=" + status + ", isSeen="
				+ isSeen +  ", order=" + order +  "]";
	}
	
	
}

