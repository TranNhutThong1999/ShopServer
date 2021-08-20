package kltn.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "shop")
public class Shop  extends AbstractId implements Serializable {
	@Id
	@GeneratedValue(generator = "my_generator")
	@GenericGenerator(name = "my_generator", strategy = "kltn.util.MyGenerator")
	@Column(nullable = false)
	private String id;
	private String nameShop;
	private String avatar;
	private String phone;
	private String email;
	private String password;
	private boolean enable;
	@Column(unique = true)
	private String code;
	private String hotLine;
	private String website;
	private String otp;
	private Timestamp expireOtp;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	private List<Product> products;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.REMOVE)
	private List<Comment> comment;

	public String generateToken() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		this.otp = String.format("%06d", number);
		return this.otp;
	}

	public boolean isAfterTime() {
		if (this.expireOtp == null) {
			return true;
		}
		Calendar time = Calendar.getInstance();
		Timestamp timetamp = new Timestamp(time.getTime().getTime());
		return timetamp.after(this.expireOtp);
	}

	public void setTimeTokenFuture(int minutes) {
		Calendar time = Calendar.getInstance();
		time.add(Calendar.MINUTE, minutes);
		this.expireOtp = new Timestamp(time.getTime().getTime());
	}

	public boolean checkOTP(String otp) {
		return this.otp.equals(otp);
	}
}