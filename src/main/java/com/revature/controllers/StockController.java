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

import com.revature.models.Stock;
import com.revature.models.User;
import com.revature.services.StockService;
import com.revature.services.UserService;

@RestController
@RequestMapping("/stocks")
public class StockController {
	
	@Autowired
	private StockService sService;
	
	@GetMapping
	public ResponseEntity<List<Stock>> findAll(){
		return ResponseEntity.ok(sService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Stock> findByID(@PathVariable("id") int id){
		return ResponseEntity.ok(sService.findByID(id));
	}
	
	@PostMapping
	public ResponseEntity<Stock> insert(@RequestBody Stock s){
		return ResponseEntity.accepted().body(sService.insert(s));
	}
	
	@PutMapping
	public ResponseEntity<Stock> update(@RequestBody Stock s){
		return ResponseEntity.accepted().body(sService.update(s));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id")int id){
		return ResponseEntity.accepted().body(sService.delete(id));
	}
}
