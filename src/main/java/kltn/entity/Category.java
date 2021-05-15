package kltn.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category extends Abstract{
	private String name;
	private String image;
	@OneToMany(mappedBy = "category")
	private List<SubCategory> subCategories;
	
}
