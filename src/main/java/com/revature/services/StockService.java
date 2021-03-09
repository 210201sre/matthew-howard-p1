package com.revature.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.StockNotFoundException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Stock;
import com.revature.models.User;
import com.revature.repositories.StockRepository;

@Service
public class StockService {
	
	@Autowired
	private HttpServletRequest req;
	
	@Autowired
	private StockRepository sDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(StockService.class);
	
	public List<Stock> findAll() {
		MDC.put("event", "select");
		logger.info("Retrieving all stocks");
		MDC.clear();
		return sDAO.findAll();
	}

	public Stock findByID(int id) {
		MDC.put("event", "select");
		MDC.put("accountID", Integer.toString(id));
		logger.info("Retrieving stock by ID");
		MDC.clear();
		return sDAO.findById(id)
				.orElseThrow(() -> new StockNotFoundException(String.format("No stock with id = %d", id)));
	}

	public Stock insert(Stock s) {
		MDC.put("event", "create");
		logger.info("Creating stock");
		MDC.clear();
		if(s.getId() != 0) {
			throw new RuntimeException("Create stock id must be 0");
		}
		
		s = sDAO.save(s);
		MDC.put("stockID", Integer.toString(s.getId()));
		logger.info("Account created");
		MDC.clear();
		
		return s;
	}

	public Stock update(Stock s) {
		MDC.put("event", "update");
		logger.info("Updating stock");
		MDC.clear();
		if(!sDAO.existsById(s.getId())) {
			throw new StockNotFoundException(String.format("No stock with id = %d", s.getId()));
		}
		
		s = sDAO.save(s);
		MDC.put("stockID", Integer.toString(s.getId()));
		logger.info("Stock updated");
		MDC.clear();

		
		return s;
	}
	public boolean delete(int id) {
		MDC.put("event", "delete");
		logger.info("Deleting stock");
		MDC.put("accountID", Integer.toString(id));
		if(!sDAO.existsById(id)) {
			logger.info("Stock does not exist");
			MDC.clear();
			return false;
		}
		sDAO.deleteById(id);
		logger.info("Stock deleted");
		MDC.clear();
		
		return true;
	}
}
