package kltn.api.output;

import java.util.List;

import lombok.Data;

@Data
public class CategoryOutPut {
	private int id;
	private String name;
	private String image;
	private List<CategoryOutPut> subCategories;
}
