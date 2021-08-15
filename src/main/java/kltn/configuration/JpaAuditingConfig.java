package kltn.configuration;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
	@Bean
	public AuditorAwareImpl auditorProvider() {
		return new AuditorAwareImpl();
	}

	public static class AuditorAwareImpl implements AuditorAware<String> {

		@Override
		public Optional<String> getCurrentAuditor() {
			String username = "";
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				 username = SecurityContextHolder.getContext().getAuthentication().getName();
			}
			return Optional.of(username);
		}
	}
}
