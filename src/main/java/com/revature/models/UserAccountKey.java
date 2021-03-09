package com.revature.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserAccountKey implements Serializable{
	
	@Column(name = "userid")
	int userID;
	
	@Column(name = "accountid")
	int accountID;
	
}
