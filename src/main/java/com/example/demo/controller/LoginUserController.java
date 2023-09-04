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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.UserAppUserDetailService;
import com.example.demo.config.UserAppUserDetails;
import com.example.demo.entities.AuthRequest;
import com.example.demo.entities.UserApp;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;

@Controller
public class LoginUserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAppUserDetailService userAppUserDetailService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private Boolean Authenticated(String token, UserDetails user)
	{
		return jwtService.validateToken(token, userAppUserDetailService.loadUserByUsername(jwtService.extractUsername(token)));
	}
	
	@GetMapping("/")
	public String LoginPage(Model model)
	{
		System.out.println("test");
		model.addAttribute("User", new AuthRequest());
		return "Login";
	}
	
	@GetMapping("/Home")
	public String HomePage(Model model)
	{
		try 
		{
			String token = jwtService.getExistingToken();
			UserDetails user = userAppUserDetailService.loadUserByUsername(jwtService.extractUsername(token));
			if (Authenticated(token, user))
			{
				return "Home";			
			}
			else
			{
				return "Login";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}

	@GetMapping("/Account")
	public String Account(Model model)
	{
		try 
		{
			String token = jwtService.getExistingToken();
			UserDetails user = userAppUserDetailService.loadUserByUsername(jwtService.extractUsername(token));
			if (Authenticated(token, user))
			{
				return "Account";			
			}
			else
			{
				return "Login";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	@PostMapping("/Home")
    public String authenticateAndGetToken(@ModelAttribute ("User") AuthRequest authRequest, Model model) {
		try
		{
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (authentication.isAuthenticated()) {
	            jwtService.setExistingToken(jwtService.generateToken(authRequest.getUsername()));
	            return HomePage(model);
	        } else {
	            throw new UsernameNotFoundException("invalid user request !");
	        }
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	
}
