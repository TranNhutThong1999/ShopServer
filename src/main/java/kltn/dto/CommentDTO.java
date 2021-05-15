package kltn.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
public class CommentDTO extends AbstractDTO{
	private String content;
	private List<RepliesDTO> replies;
	private int userId;
	private int productId;
}
