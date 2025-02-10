package lt.ca.javau11.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SecurityConfig configures Spring Security for the BellTime application.
 * <p>
 * This configuration disables CSRF (appropriate for REST APIs), defines basic authorization rules,
 * and enables HTTP Basic authentication using the modern DSL approach.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity instance to customize.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");
        
        http
            // Disable CSRF protection for REST APIs (adjust as needed for your project)
            .csrf(csrf -> csrf.disable())
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // Allow public access to API endpoints
                .anyRequest().authenticated()             // Require authentication for all other requests
            )
            // Enable HTTP Basic authentication using the new DSL
            .httpBasic(withDefaults());
        
        log.info("Security filter chain configured successfully");
        return http.build();
    }
}
