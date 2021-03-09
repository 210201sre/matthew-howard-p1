package com.revature.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.LoginException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.AccountStockRepository;
import com.revature.repositories.StockRepository;
import com.revature.repositories.UserAccountRepository;
import com.revature.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private HttpServletRequest req;
	
	@Autowired
	private UserRepository uDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	public List<User> findAll(){
		MDC.put("event", "select");
		logger.info("Retrieving all users");
		MDC.clear();
		return uDAO.findAll()
;	}
	
	public User findByID(int id) {
		MDC.put("event", "select");
		MDC.put("userID", Integer.toString(id));
		logger.info("Retrieving user by ID");
		MDC.clear();
		return uDAO.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format("No user with id = %d", id)));
	}
	
	public User insert(User u) {
		MDC.put("event", "create");
		logger.info("Creating user");
		MDC.clear();
		if(u.getId() != 0) {
			throw new RuntimeException("Create user id must be 0");
		}
		
		u = uDAO.save(u);
		MDC.put("userID", Integer.toString(u.getId()));
		logger.info("User created");
		MDC.clear();
		
		return u;
	}
	
	public User update(User u) {
		MDC.put("event", "update");
		logger.info("Updating user");
		MDC.clear();
		if(!uDAO.existsById(u.getId())) {
			throw new UserNotFoundException(String.format("No user with id = %d", u.getId()));
		}
		
		u = uDAO.save(u);
		MDC.put("userID", Integer.toString(u.getId()));
		logger.info("User updated");
		MDC.clear();
		
		return u;
	}
	
	public boolean delete(int id) {
		MDC.put("event", "delete");
		logger.info("Deleting user");
		MDC.put("userID", Integer.toString(id));
		if(!uDAO.existsById(id)) {
			logger.info("User does not exist");
			MDC.clear();
			return false;
		}
		uDAO.deleteById(id);
		logger.info("User deleted");
		MDC.clear();
		
		return true;
	}
	
	public User login(String username, String password) {
		MDC.put("event", "login");
		logger.info("Logging in");
		MDC.clear();
		User u = uDAO.findByUsername(username)
							.orElseThrow(() -> new LoginException(String.format("No User with username = %s", username)));
		MDC.put("userID", Integer.toString(u.getId()));
		MDC.put("password", password);
		
		if (!u.getPassword().equals(password)) {
			throw new LoginException("Password is incorrect");
		}
		
		HttpSession session = req.getSession();
		session.setAttribute("currentUser", u);
		logger.info("Logged in");
		MDC.clear();
		
		return u;
	}
	
	public void logout() {
		MDC.put("event", "logout");
		logger.info("Logging out");
		MDC.clear();
		HttpSession session = req.getSession(false);
		
		if(session == null) {
			return;
		}
		
		session.invalidate();
		logger.info("Logged out");
		MDC.clear();
	}

}
