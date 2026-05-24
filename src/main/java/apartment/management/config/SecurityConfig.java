package apartment.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF korumasını kapatıyoruz (H2 ve API testleri için zorunlu)
                .csrf(csrf -> csrf.disable())

                // 2. KRİTİK: H2 Console'un iframe'ler içinde açılmasına izin veriyoruz
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        // 3. H2 Console yoluna şifresiz izin veriyoruz
                        .requestMatchers("/h2-console/**").permitAll()

                        // 4. Swagger ve OpenAPI yollarına izin veriyoruz
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 5. index.html, statik dosyalar ve kendi API'lerimize izin veriyoruz
                        .requestMatchers("/", "/index.html", "/static/**", "/api/**").permitAll()

                        // Diğer her şeyi kilitliyoruz (varsa)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}