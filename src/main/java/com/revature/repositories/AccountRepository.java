package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

}
