package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "photo")
public class Photo extends Abstract{
	private String name;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
}
