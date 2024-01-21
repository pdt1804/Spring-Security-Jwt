package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.UserAppUserDetailService;
import com.example.demo.config.UserAppUserDetails;
import com.example.demo.entities.AuthRequest;
import com.example.demo.entities.UserApp;
import com.example.demo.services.JwtService;
import com.example.demo.services.ResourceStoredService;
import com.example.demo.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginUserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAppUserDetailService userAppUserDetailService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ResourceStoredService resourceStoredService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public String getUserName(HttpServletRequest request) // Get JWT from Authorization
	{
		var authHeader = request.getHeader("Authorization");
		if (authHeader.startsWith("Bearer "))
		{
			return jwtService.extractUsername(authHeader.substring(7));
		}
		return null;
	}
	
	@GetMapping("/Authenticate")
    public String authenticateAndGetToken(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord, HttpServletRequest request, HttpServletResponse response) 
    {	
		try
		{
			String authen = userService.Authenticate(userName, passWord);
			
			if (authen.equals("Failed"))
			{
				return "Failed";	
			}
			else
			{
				Cookie cookie = new Cookie("token" + userName, userService.Authenticate(userName, passWord));
				response.addCookie(cookie);
				return "Successful";
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return "Failed";	
		}	
	}
	
	@GetMapping("/GetUserInformation")
    public UserApp GetUserInformation(HttpServletRequest request) 
    {	
		try
		{
			String userName = getUserName(request);
			
			if (userName == null)
			{
				return null; // invalid
			}
			else
			{
				var cookies = request.getCookies();
				
				for (var cookie : cookies)
				{
					if (cookie.getName().equals("token" + userName))
					{
						return userService.GetUserByUsername(userName);
					}
				}	
				
				return null;
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;	
		}	
	}
	
	@PostMapping("/CreateUser")
    public void CreateUser(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) 
	{	
		try
		{
	        userService.AddUser(new UserApp(userName, passWord, null, "ADMIN"));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	
}
