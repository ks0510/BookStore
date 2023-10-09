package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.model.Book;
//import com.example.demo.Customer;
import com.example.demo.dto.Response;

public interface IBookStoreService {
	
	public ResponseEntity<Response> add(Book book);
	
	public ResponseEntity<Response> updatePrice(int bookId,int price);
	
	public String delete(int id);
	
	public  ResponseEntity<Response> updateQuantity(int id,int quantity);
	
	public List<Book> getAllBooks();
	
	
	
	
	

}
