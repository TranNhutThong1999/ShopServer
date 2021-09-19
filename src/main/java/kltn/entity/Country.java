package kltn.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "county")
public class Country extends Abstract {
	private String name;
	private String code;
}
