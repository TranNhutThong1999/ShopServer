package kltn.util;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.spi.QueryImplementor;


public class MyGenerator implements IdentifierGenerator {
	private String prefix = "S";
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String query = "select id from Shop";
		QueryImplementor<String> q = session.createQuery(query, String.class);
		
		List<String> id = q.getResultList();
		Stream<String> ids = session.createQuery(query, String.class).stream();
		Long max = ids.map(o -> o.replace(prefix, "")).mapToLong(Long::parseLong).max().orElse(0L);
		return prefix + (String.format("%03d", max + 1));
	}
}
