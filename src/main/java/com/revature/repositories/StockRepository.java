package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
