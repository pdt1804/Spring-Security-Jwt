package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.UserApp;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
	private JwtService jwtService;
    
    @Autowired
	private AuthenticationManager authenticationManager;
	
	public List<UserApp> GetAllUser()
	{
		return userRepo.findAll();
	}
	
	public UserApp GetUserByUsername(String username)
	{
		Optional<UserApp> user = userRepo.findById(username);
		return user.get();
	}
	
	public String Authenticate(String userName, String passWord)
	{
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, passWord));
		
		if (authenticate.isAuthenticated())
		{
			return jwtService.generateToken(userName);
		}
		return "Failed";
	}
	
	public void AddUser(UserApp user)
	{
		try
		{
			System.out.println(user.getPassword());
			var pass = passwordEncoder.encode(user.getPassword());
			System.out.println(pass);
			user.setPassword(pass);
			userRepo.save(user);

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void UpdateUser(UserApp user)
	{
		try
		{
			Optional<UserApp> existingUser = userRepo.findById(user.getUsername());
			existingUser.get().setPassword(user.getPassword());
			existingUser.get().setDescription(user.getDescription());
			userRepo.save(user);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void DeleteUser(UserApp user)
	{
		try
		{
			userRepo.delete(user);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
