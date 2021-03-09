package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Account;
import com.revature.models.AccountStock;
import com.revature.models.Stock;

public interface AccountStockRepository extends JpaRepository<AccountStock, Integer> {
	public List<AccountStock> findByAccountId(int id);
	public List<AccountStock> findByStockId(int id);
	public Optional<AccountStock> findByAccountAndStock(Account account, Stock stock);
}
