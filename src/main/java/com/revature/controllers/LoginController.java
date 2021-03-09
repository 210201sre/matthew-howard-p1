package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Login;
import com.revature.models.User;
import com.revature.services.UserService;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody Login login) {
		
		return ResponseEntity.ok(userService.login(login.getUsername(), login.getPassword()));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		userService.logout();
		
		return ResponseEntity.accepted().build();
	}
}
