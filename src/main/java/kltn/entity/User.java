package kltn.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User extends Abstract{
	private String firstName;
	private String lastName;
	private String pictureURL;
	private String phone;
	private String gmail;
	private String dayOfBirth;
	private boolean enabled; //defaul: false is not verify
	private String otp;
	private Timestamp expireOtp;
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private int gender; //true is boy
	private String address;

	
	@OneToMany(mappedBy = "shop")
	private List<Product> products;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	@OneToMany(mappedBy = "user")
	private List<Action> actions;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Following> follow;
	
	public String generateToken() {
		Random rnd = new Random();
	    int number = rnd.nextInt(999999);
		this.otp = String.format("%06d", number);
		return this.otp;
	}
	
	public boolean isAfterTime() {
		if(this.expireOtp == null) {
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
