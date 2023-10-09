package com.example.demo.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Orders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int orderId;
	@ManyToMany(cascade = CascadeType.ALL)
	List<CartBook> orderList;
	int totalPrice;

}
