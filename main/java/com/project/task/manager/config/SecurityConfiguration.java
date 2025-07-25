package com.project.task.manager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.project.task.manager.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider; 
	
    @Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth
						.requestMatchers("/api/v1/auth/**").permitAll()
						.anyRequest().authenticated())
//				.formLogin(form -> form.disable())
				.sessionManagement(
						session -> session
						.sessionCreationPolicy
						(SessionCreationPolicy.STATELESS))
				.authenticationProvider
				(authenticationProvider) 
				.addFilterBefore
				(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
}
