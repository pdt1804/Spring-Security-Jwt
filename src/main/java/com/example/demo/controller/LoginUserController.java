package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entities.UserApp;
import com.example.demo.services.UserService;

@Controller
public class LoginUserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String LoginPage(Model model)
	{
		model.addAttribute("User", new UserApp());
		return "Login";
	}
	
	@GetMapping("/Home")
	public String HomePage(Model model)
	{
		return "Home";
	}
	
}
