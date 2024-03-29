package kltn.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User extends AbstractId{
	@Id
	@Column(nullable = false)
	private String id;
	private String firstName;
	private String lastName;
	private String pictureURL;
	private String phone;
	private String email;
	private String dayOfBirth;
	private boolean enabled; //defaul: false is not verify
	private String otp;
	private Timestamp expireOtp;
	private int gender; //true is boy
	private String address;

	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Action> actions;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Comment> comment;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Notification> notifications;
	
	
}
