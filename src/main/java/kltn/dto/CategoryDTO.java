package kltn.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoryDTO extends AbstractDTO{
	private String name;
	private String image;
	private int parentId;
	private List<CategoryDTO> SubCategorys;
}
