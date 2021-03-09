package com.revature.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.LoginException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.UserAccountRepository;
import com.revature.repositories.UserRepository;

@Service
public class AccountService {

	@Autowired
	private HttpServletRequest req;
	
	@Autowired
	private AccountRepository aDAO;
	
	@Autowired
	private UserAccountRepository uaDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	public List<Account> findAll(){
		MDC.put("event", "select");
		logger.info("Retrieving all accounts");
		MDC.clear();
		return aDAO.findAll();
	}
	
	public Account findByID(int id) {
		MDC.put("event", "select");
		MDC.put("accountID", Integer.toString(id));
		logger.info("Retrieving account by ID");
		MDC.clear();
		return aDAO.findById(id)
				.orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", id)));
	}
	
	public Account insert(Account a) {
		HttpSession session = req.getSession(false);
		MDC.put("event", "create");
		logger.info("Creating account");
		MDC.clear();
		if(a.getId() != 0) {
			throw new RuntimeException("Created account id must be 0");
		}
		UserAccount ua = new UserAccount();
		ua.setAccount(a);
		ua.setUser((User) session.getAttribute("currentUser"));
		a = aDAO.save(a);
		uaDAO.save(ua);
		MDC.put("accountID", Integer.toString(a.getId()));
		logger.info("Account created");
		MDC.clear();
		
		return a;
	}
	
	public Account update(Account a) {
		MDC.put("event", "update");
		logger.info("Updating account");
		MDC.clear();
		if(!aDAO.existsById(a.getId())) {
			throw new AccountNotFoundException(String.format("No account with id = %d", a.getId()));
		}
		
		a = aDAO.save(a);
		MDC.put("accountID", Integer.toString(a.getId()));
		logger.info("Account updated");
		MDC.clear();
		
		return a;
	}
	
	public boolean delete(int id) {
		MDC.put("event", "delete");
		MDC.put("accountID", Integer.toString(id));
		logger.info("Deleting account");
		
		if(!aDAO.existsById(id)) {
			logger.info("Account does not exist");
			MDC.clear();
			return false;
		}
		aDAO.deleteById(id);
		logger.info("Account deleted");
		MDC.clear();
		
		return true;
	}

}