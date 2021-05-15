package kltn.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "shop")
public class Shop extends Abstract{
	private String nameBoss;
	private String avatar;
	private String address;
	private String nameShop;
	private String userName;
	private String phone;
	private String gmail;
	private String password;
	private boolean enable;
	@Column(unique = true)
	private String code;
	private String hotLine;
	private String website;
	
	@OneToMany(mappedBy = "shop")
	private List<Product> products;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
}
