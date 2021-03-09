package com.revature.models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts", schema="proj0")
@Data @NoArgsConstructor @AllArgsConstructor
public class Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="balance")
	private double balance;
	//@Transient
	@OneToMany(mappedBy="account",cascade=CascadeType.ALL)
	//@JsonIgnore
	private List<AccountStock> AccountStocks;
	@JsonIgnore
	@OneToMany(mappedBy="account", cascade=CascadeType.ALL)
	private List<UserAccount> UserAccounts;
}