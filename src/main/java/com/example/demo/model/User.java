
package com.example.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String username;
	private String fullName;
	private String email;
	private String password;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<Address> addresses;
	
	@OneToOne(cascade = CascadeType.ALL)
	Cart cart;
	
	@OneToMany(cascade =CascadeType.ALL)
	List<Orders> order;

}
