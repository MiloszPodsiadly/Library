package com.kodilla.library.config;

import com.kodilla.library.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.kodilla.library.jwt.JwtService;
import com.kodilla.library.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        return new JwtAuthFilter(jwtService, userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ⬇️ publiczne GET-y
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/statistics/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/favorites/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
