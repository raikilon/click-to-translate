package ch.clicktotranslate.gateway.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.cors")
public record CorsProperties(List<String> allowedOrigins) {

	public CorsProperties {
		if (allowedOrigins == null) {
			allowedOrigins = List.of();
		}
		else {
			allowedOrigins = allowedOrigins.stream()
				.map(origin -> origin == null ? "" : origin.trim())
				.filter(origin -> !origin.isBlank())
				.toList();
		}
	}

}
