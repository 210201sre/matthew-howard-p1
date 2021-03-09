package com.revature.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.AuthorizationException;
import com.revature.models.User;
import com.revature.models.UserAccount;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.UserAccountRepository;

@Service
public class AuthorizationService {

	@Autowired
	private HttpServletRequest req;
	
	@Autowired
	private UserAccountRepository uaDAO;
	
	@Autowired
	private AccountRepository aDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
	
	// The ID that we pass in represents the ID of the User resource that is being manipulated
	public void guardByUserID(int userID) {
		
		HttpSession session = req.getSession(false);

		if(session == null || session.getAttribute("currentUser") == null) {
			throw new AuthorizationException("No user is logged in");
		}
		
		User currentUser = (User) session.getAttribute("currentUser");
		
		if(userID != currentUser.getId()) {		
			throw new AuthorizationException("Current user cannot access this resource");
		}
	}

	public void guardByAccountID(int accountID) {
		HttpSession session = req.getSession(false);

		if(session == null || session.getAttribute("currentUser") == null) {
			throw new AuthorizationException("No user is logged in");
		}
		
		User currentUser = (User) session.getAttribute("currentUser");
		
		UserAccount ua = uaDAO.findByUserAndAccount(currentUser, aDAO.findById(accountID).orElse(null)).orElse(null);
		
		if(ua == null) {
			throw new AuthorizationException("Current user cannot access this resource");
		}
		
	}
}
