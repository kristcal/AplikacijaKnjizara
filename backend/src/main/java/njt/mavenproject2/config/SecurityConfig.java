package njt.mavenproject2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1️⃣ — isključi CSRF za REST API
            .csrf(csrf -> csrf.disable())
            // 2️⃣ — dozvoli CORS za frontend (React)
            .cors(Customizer.withDefaults())
            // 3️⃣ — pravila pristupa po rutama
            .authorizeHttpRequests(auth -> auth
                // login je otvoren
                .requestMatchers("/api/auth/**").permitAll()
                // svi mogu da vide knjige
                .requestMatchers(HttpMethod.GET, "/api/knjige/**").permitAll()
                // sve ostalo (porudzbine, CRUD...) dozvoli za sad
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
            "http://localhost:3000",      // lokalni frontend
            "http://172.20.10.3:3000"     // IP tvoja mreža
        ));
        cfg.setAllowedMethods(List.of(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 4️⃣ — dozvoli sve /api putanje
        source.registerCorsConfiguration("/api/**", cfg);
        return source;
    }
}
