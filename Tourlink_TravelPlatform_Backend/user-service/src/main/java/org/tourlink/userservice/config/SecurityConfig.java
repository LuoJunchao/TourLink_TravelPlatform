package org.tourlink.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.tourlink.common.security.SecurityConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(SecurityConstants.LOGIN_ENDPOINT, SecurityConstants.REGISTER_ENDPOINT).permitAll()
                .requestMatchers("/api/users/batch/basic","/api/users/auth", "/api/users/*/basic").permitAll()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
