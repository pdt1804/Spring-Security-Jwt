package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.entities.UserApp;
import com.example.demo.repository.UserRepository;

@Service
public class ResourceStoredService {

	private String bearerToken;
	private String currentUsername;
	
	@Autowired
	private UserRepository userRepository;
	
	public UserApp getUser()
	{
		return userRepository.getById(currentUsername);
	}

	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}
	
	
}
