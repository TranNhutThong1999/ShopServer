package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="detail")
public class Detail extends Abstract{
	private String provideByShop;
	private String trademark;
	private String madeBy;
	private String size;
	private String color;
	private String material;
}
