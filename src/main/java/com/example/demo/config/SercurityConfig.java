package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
public class SercurityConfig{

	@Autowired
	private UserRepository userRepository;
	
	// authentication
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserAppUserDetailService();
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers("/").permitAll()
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/New").permitAll()
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/Get").permitAll()
                    .and()
                    .authorizeHttpRequests()
                    .anyRequest().authenticated()
                    .and().formLogin()// trả về page login nếu chưa authenticate
                    .and().build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
