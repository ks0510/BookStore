package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.User;


public interface IUserDataRepo extends JpaRepository<User,Integer> {
	
	public User findByEmailAndPassword(String email,String password);
	
	
	
	

}
