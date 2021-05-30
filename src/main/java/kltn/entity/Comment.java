package kltn.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment extends Abstract{
	private String content;

	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;
	
	
	@OneToMany(mappedBy = "comment")
	private List<Comment> replies;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
}
