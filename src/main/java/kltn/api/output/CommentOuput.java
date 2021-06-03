package kltn.api.output;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class CommentOuput {
	private int id;
	private String content;
	private int parentId;
	private int productId;
	private int userId;
	private String fullName;
	private String userAvatar;
	private Date createOn;
	private List<CommentOuput> commentChild;
}