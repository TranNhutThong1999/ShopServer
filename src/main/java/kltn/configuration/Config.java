package kltn.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class Config {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper m = new ModelMapper();
		m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		m.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		return m;
	}
//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//		source.setBasenames("messages/messages");
//		source.setDefaultEncoding("UTF-8");
//		source.setUseCodeAsDefaultMessage(true);
//
//		return source;
//	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
