package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address extends Abstract{
	private String location;
	
	@ManyToOne
	@JoinColumn(name = "provincial_id")
	private Provincial provincial;
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne
	@JoinColumn(name = "wards_id")
	private Wards Wards;
	
	@OneToOne
	@JoinColumn(name="shop_id")
	private Shop shop;
}
