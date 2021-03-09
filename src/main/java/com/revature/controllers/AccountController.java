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

import com.revature.models.Account;
import com.revature.models.Stock;
import com.revature.services.AccountService;
import com.revature.services.AuthorizationService;
import com.revature.services.StockService;
import com.revature.services.TransferService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private AccountService aService;
	
	@Autowired
	private TransferService tService;
	
	@Autowired
	private AuthorizationService authService;
	
	@GetMapping
	public ResponseEntity<List<Account>> findAll(){
		return ResponseEntity.ok(aService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Account> findByID(@PathVariable("id") int id){
		authService.guardByAccountID(id);
		return ResponseEntity.ok(aService.findByID(id));
	}
	
	@PostMapping
	public ResponseEntity<Account> insert(@RequestBody Account a){
		return ResponseEntity.accepted().body(aService.insert(a));
	}
	
	@PutMapping
	public ResponseEntity<Account> update(@RequestBody Account a){
		authService.guardByAccountID(a.getId());
		return ResponseEntity.accepted().body(aService.update(a));
	}
	
	@PutMapping("/{id}/buy/{stockid}/{amount}")
	public ResponseEntity<Account> buy(@PathVariable("id") int accountID, @PathVariable("stockid") int stockID, @PathVariable("amount") double amount){
		authService.guardByAccountID(accountID);
		return ResponseEntity.accepted().body(tService.buy(accountID, stockID, amount));
	}
	@PutMapping("/{id}/sell/{stockid}/{amount}")
	public ResponseEntity<Account> sell(@PathVariable("id") int accountID, @PathVariable("stockid") int stockID, @PathVariable("amount") double amount){
		authService.guardByAccountID(accountID);
		return ResponseEntity.accepted().body(tService.sell(accountID, stockID, amount));
	}
	@PutMapping("/{id}/transfer/{sellid}/{stockid}/{amount}")
	public ResponseEntity<Account> transfer(@PathVariable("id") int buyID, @PathVariable("sellid") int sellID, @PathVariable("stockid") int stockID, @PathVariable("amount") double amount){
		authService.guardByAccountID(buyID);
		return ResponseEntity.accepted().body(tService.transfer(buyID,sellID, stockID, amount));
	}
	@PutMapping("/{id}/addfunds/{amount}")
	public ResponseEntity<Account> addFunds(@PathVariable("id") int id, @PathVariable("amount") double amount){
		authService.guardByAccountID(id);
		return ResponseEntity.accepted().body(tService.addFunds(id, amount));
	}
	@PutMapping("/{id}/withdrawlfunds/{amount}")
	public ResponseEntity<Account> withdrawlFunds(@PathVariable("id") int id, @PathVariable("amount") double amount){
		authService.guardByAccountID(id);
		return ResponseEntity.accepted().body(tService.withdrawlFunds(id, amount));
	}
	@PutMapping("/{id}/transferfunds/{toid}/{amount}")
	public ResponseEntity<Account> transferFunds(@PathVariable("id") int id, @PathVariable("toid") int toID, @PathVariable("amount") double amount){
		authService.guardByAccountID(id);
		return ResponseEntity.accepted().body(tService.transferFunds(id,toID, amount));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id")int id){
		authService.guardByAccountID(id);
		return ResponseEntity.accepted().body(aService.delete(id));
	}
}