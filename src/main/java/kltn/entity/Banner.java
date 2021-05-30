package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "banner")
public class Banner extends Abstract{
	private String name;
	private String url;
}
