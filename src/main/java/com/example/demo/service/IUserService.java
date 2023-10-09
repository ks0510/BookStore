package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.example.demo.dto.Response;
import com.example.demo.model.Book;
import com.example.demo.model.User;

public interface IUserService {
	
	public ResponseEntity<Response> addUser(User user);
	
	public String login(String email,String password);
	
	public ResponseEntity<Response> addBook(String bookName,String token);
	
	public ResponseEntity<Response> buyBook(String token);
	
	public String deleteUser(String token);
	
	public ResponseEntity<Response> updateUser(String token,User user);
	
	public String increaseCartValue(String token,String bookName);
	
	public String decreaseCartValue(String token,String bookName);
	
	public ResponseEntity<Response> removeFromCart(String token,String bookName);
	
	public ResponseEntity<Response> showOrderHistory(String token);
	
	public List<Book> getAllBooks();
	
	public List<Book> showBookLowtoHigh();
	
	public List<Book> showBookNewtoOld();
	
	public ResponseEntity<Response> findByAuthor(String author);
}
