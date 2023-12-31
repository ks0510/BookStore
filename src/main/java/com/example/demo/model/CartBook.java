package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class CartBook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int cartBookId;
	String bookName;
	int bookPrice;
	int bookQuantity;
	

}
