package kltn.dto;

import lombok.Data;

@Data
public class FollowingDTO extends AbstractDTO{
	private int userId;
	private int shopId;
}
