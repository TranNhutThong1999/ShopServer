package kltn.dto;



import lombok.Data;

@Data
public class RepliesDTO extends AbstractDTO{
	private String content;
	private int userId;
	private int commentId;
}
