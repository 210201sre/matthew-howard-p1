package com.revature.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.StockNotFoundException;
import com.revature.exceptions.TransferException;
import com.revature.models.Account;
import com.revature.models.AccountStock;
import com.revature.models.Stock;
import com.revature.repositories.AccountRepository;
import com.revature.repositories.AccountStockRepository;
import com.revature.repositories.StockRepository;
import com.revature.repositories.UserRepository;

@Service
public class TransferService {
	@Autowired
	private HttpServletRequest req;
	@Autowired
	private UserRepository uDAO;
	@Autowired
	private AccountRepository aDAO;
	@Autowired
	private StockRepository sDAO;
	@Autowired
	private AccountStockRepository asDAO;
	private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
	
	public Account addFunds(int id, double amount) {
		MDC.put("event", "add funds");
		MDC.put("accountID", Integer.toString(id));
		MDC.put("amount", Double.toString(amount));
		logger.info("adding funds to account");
		
		Account tmp = aDAO.findById(id).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", id)));
		tmp.setBalance(tmp.getBalance() + amount);
		tmp = aDAO.save(tmp);
		logger.info("funds sucessfully added");
		MDC.clear();
		
		return tmp;
	}
	
	public Account withdrawlFunds(int id, double amount) {
		MDC.put("event", "withdrawl funds");
		MDC.put("accounID", Integer.toString(id));
		MDC.put("amount", Double.toString(amount));
		logger.info("Withdrawling funds");
		Account tmp = aDAO.findById(id).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", id)));
		tmp.setBalance(tmp.getBalance() - amount);
		if(tmp.getBalance() < 0) {
			throw new TransferException(String.format("Insuffiecent funds. Account balance = %f, while withdrawl amount = %f", tmp.getBalance(), amount));
		}
		logger.info("funds withdrawn");
		MDC.clear();
		
		return aDAO.save(tmp);
	}
	public Account transferFunds(int fromID, int toID, double amount) {
		MDC.put("event", "fund transfer");
		MDC.put("fromID", Integer.toString(fromID));
		MDC.put("toID", Integer.toString(toID));
		MDC.put("amount", Double.toString(amount));
		logger.info("transferring funds");
		
		Account from = aDAO.findById(fromID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", fromID)));
		from.setBalance(from.getBalance() - amount);
		if(from.getBalance() < 0) {
			throw new TransferException(String.format("Insuffiecent funds. Account balance = %f, while withdral amount = %f", from.getBalance(), amount));
		}
		Account to = aDAO.findById(toID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", toID)));
		to.setBalance(to.getBalance() + amount);
		
		aDAO.save(from);
		aDAO.save(to);
		
		logger.info("funds transferred");
		MDC.clear();
		
		return from;
	}
	
	public Account buy(int accountID, int stockID, double amount) {
		MDC.put("event", "buy");
		MDC.put("accountID", Integer.toString(accountID));
		MDC.put("stockID", Integer.toString(stockID));
		MDC.put("amount", Double.toString(amount));
		logger.info("buying stock");
		
		Account account = aDAO.findById(accountID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", accountID)));
		Stock stock = sDAO.findById(stockID).orElseThrow(() -> new StockNotFoundException(String.format("No stock with id = %d", stockID)));
		AccountStock  as = asDAO.findByAccountAndStock(account, stock).orElse(null);
		
		double price = stock.getValue()*amount;
		if(price > account.getBalance()) {
			throw new TransferException(String.format("Insuffiecent funds. Account balance = %f, while price = %f", account.getBalance(), price));
		}
		
		if(as != null) {
			account.setBalance(account.getBalance()-price);
			as.setAmount(as.getAmount()+amount);
			
			aDAO.save(account);
			asDAO.save(as);
			
			logger.info("Stock bought");
			MDC.clear();
			
			return account;
			
		}
		as = new AccountStock();
		as.setId(0);
		as.setAccount(account);
		as.setStock(stock);
		as.setAmount(amount);
		
		account.setBalance(account.getBalance()-price);
		
		aDAO.save(account);
		asDAO.save(as);
		
		logger.info("Stock bought");
		MDC.clear();
		
		return account;
	}
	
	public Account sell(int accountID, int stockID, double amount) {
		MDC.put("event", "sell");
		MDC.put("accountID", Integer.toString(accountID));
		MDC.put("stockID", Integer.toString(stockID));
		MDC.put("amount", Double.toString(amount));
		logger.info("selling stock");
		
		Account account = aDAO.findById(accountID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", accountID)));
		Stock stock = sDAO.findById(stockID).orElseThrow(() -> new StockNotFoundException(String.format("No stock with id = %d", stockID)));
		AccountStock  as = asDAO.findByAccountAndStock(account, stock).orElseThrow(()-> new TransferException(String.format("Account with id=%d does not own stock with id=%d", accountID, stockID)));
		if(as.getAmount() < amount) {
			throw new TransferException(String.format(String.format("Account with id=%d does not own enough of stock with id=%d", accountID, stockID)));
		}
		double price = stock.getValue()*amount;	
		account.setBalance(account.getBalance()+price);
		as.setAmount(as.getAmount()-amount);
		
		if(as.getAmount()==0) {
			asDAO.delete(as);
		}else {
			asDAO.save(as);
		}
		aDAO.save(account);
		
		logger.info("Stock sold");
		MDC.clear();	
		
		return account;
	}
	
	public Account transfer(int buyAccountID, int sellAccountID, int stockID, double amount) {
		MDC.put("event", "transfer stock");
		MDC.put("buyAccountID", Integer.toString(stockID));
		MDC.put("sellAccountID", Integer.toString(stockID));
		MDC.put("StockID", Integer.toString(stockID));
		MDC.put("amount", Double.toString(amount));
		logger.info("transferring stock");
		
		Account sellAccount = aDAO.findById(sellAccountID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", sellAccountID)));
		Account buyAccount = aDAO.findById(buyAccountID).orElseThrow(() -> new AccountNotFoundException(String.format("No account with id = %d", buyAccountID)));
		Stock stock = sDAO.findById(stockID).orElseThrow(() -> new StockNotFoundException(String.format("No stock with id = %d", stockID)));
		AccountStock  sellAS = asDAO.findByAccountAndStock(sellAccount, stock).orElseThrow(()-> new TransferException(String.format("Account with id=%d does not own stock with id=%d", sellAccountID, stockID)));
		AccountStock  buyAS = asDAO.findByAccountAndStock(buyAccount, stock).orElseThrow(()-> new TransferException(String.format("Account with id=%d does not own stock with id=%d", buyAccountID, stockID)));
		if(sellAS.getAmount() < amount) {
			throw new TransferException(String.format(String.format("Account with id=%d does not own enough of stock with id=%d", sellAccountID, stockID)));
	}
		double price = stock.getValue()*amount;
		
		sellAccount.setBalance(sellAccount.getBalance()+price);
		sellAS.setAmount(sellAS.getAmount()-amount);
		
		if(price > buyAccount.getBalance()) {
			throw new TransferException(String.format("Insuffiecent funds. Account balance = %f, while price = %f", buyAccount.getBalance(), price));
		}
		
		buyAccount.setBalance(buyAccount.getBalance()-price);
		buyAS.setAmount(buyAS.getAmount()+amount);
			
		aDAO.save(sellAccount);
		if(sellAS.getAmount()==0) {
			asDAO.delete(sellAS);
		}else {
			asDAO.save(sellAS);
		}
		aDAO.save(buyAccount);
		asDAO.save(buyAS);
		
		logger.info("stock transferred");
		MDC.clear();
		
		return buyAccount;
	}
	
}
