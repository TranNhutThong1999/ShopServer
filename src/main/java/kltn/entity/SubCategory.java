package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table
@Entity(name = "sub_category")
public class SubCategory extends Abstract{
	private String name;
	private String imgae;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
}
