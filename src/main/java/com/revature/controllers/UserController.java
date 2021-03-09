package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.User;
import com.revature.services.AuthorizationService;
import com.revature.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService uService;
	@Autowired
	private AuthorizationService aService;
	
	@GetMapping
	public ResponseEntity<List<User>> findAll(){
		return ResponseEntity.ok(uService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findByID(@PathVariable("id") int id){
		//aService.guardByUserId(id);
		return ResponseEntity.ok(uService.findByID(id));
	}
	
	@PostMapping
	public ResponseEntity<User> insert(@RequestBody User u){
		return ResponseEntity.accepted().body(uService.insert(u));
	}
	
	@PutMapping
	public ResponseEntity<User> update(@RequestBody User u){
		aService.guardByAccountID(u.getId());
		return ResponseEntity.accepted().body(uService.update(u));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id")int id){
		aService.guardByUserID(id);
		return ResponseEntity.accepted().body(uService.delete(id));
	}
}
