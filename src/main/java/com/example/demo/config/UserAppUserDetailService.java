package com.example.demo.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entities.UserApp;
import com.example.demo.repository.UserRepository;

@Component
public class UserAppUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserApp> userApp = userRepository.findById(username);
		return userApp.map(UserAppUserDetails::new)
	              .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));	
	}

}
