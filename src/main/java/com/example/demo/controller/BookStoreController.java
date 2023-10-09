package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.dto.Response;
import com.example.demo.service.BookStoreService;

@RestController
@RequestMapping("/bookstore")
public class BookStoreController {
	
	@Autowired
	BookStoreService bookService;
	
	@Autowired
	Response response;
	
	@PostMapping("/addbook")
	public ResponseEntity<Response> addBook(@RequestBody Book book){
		return bookService.add(book);
	}
	
	@PutMapping("/updateprice/{id}")
	public ResponseEntity<Response> updatePrice(@PathVariable int id,@RequestParam int price){
		return bookService.updatePrice(id, price);
	}
	
	@DeleteMapping("/deletebook/{id}")
	public String deleteBook(@PathVariable int id) {
		return bookService.delete(id);
	}
	
	@PutMapping("/updatequantity/{id}")
	public ResponseEntity<Response> updateQuantity(@PathVariable int id,@RequestParam int quantity){
		return bookService.updateQuantity(id, quantity);
	}
	
	@GetMapping("/getallbooks")
	public List<Book> getBooks(){
		return bookService.getAllBooks();
	}
	

}
