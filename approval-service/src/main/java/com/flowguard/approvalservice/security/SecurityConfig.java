package com.flowguard.approvalservice.security;

import com.flowguard.approvalservice.filter.TraceIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final TraceIdFilter traceIdFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            TraceIdFilter traceIdFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.traceIdFilter = traceIdFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(traceIdFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, TraceIdFilter.class);

        return http.build();
    }
}
