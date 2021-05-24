package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "provincial")
public class Provincial extends Abstract{
	private String name;
	private int code;
	
}
