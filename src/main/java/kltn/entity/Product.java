package kltn.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private String code;
	private double price;
	private int sale;
	private double priceSale;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	private int avaiable;
	private int quantitySold;
	private double weight;
	private boolean isDeleted;
	
	@Column(columnDefinition = "TEXT")
	private String detail;
	
	@Column(columnDefinition = "TEXT")
	private String photos;
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="shop_id")
	private Shop shop;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Rating> ratings;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Item> item;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Action> action;
}
