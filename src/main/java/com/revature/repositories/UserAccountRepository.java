package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.models.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	public List<UserAccount> findByUserId(int id);
	public List<UserAccount> findByAccountId(int id);
	public Optional<UserAccount> findByUserAndAccount(User u, Account a);
}
