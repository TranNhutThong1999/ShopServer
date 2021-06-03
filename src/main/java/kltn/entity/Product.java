package kltn.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product extends Abstract{
	private String name;
	private String price;
	private int sale;
	
	@OneToMany(mappedBy = "product")
	private List<Comment> comments;
	
	@Column(columnDefinition = "TEXT")
	
	private String description;
	private int quantity;
	//private ProductStatus status;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "detail_id")
	private Detail detail;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<Photo> photos;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Shop shop;
}
