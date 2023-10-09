package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.Response;
import com.example.demo.model.Book;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/registeration")
	public ResponseEntity<Response> doRegisteration(@RequestBody  User user){
		return userService.addUser(user);
		
	}
	
	@GetMapping("/showbooks")
	public List<Book> showAllBooks(){
		return userService.getAllBooks();
	}
	
	@GetMapping("/login")
	public String doLogin(@RequestParam String email,@RequestParam String password) {
		
		return userService.login(email, password);
	}
	
	@PostMapping("/addtocart")
	public ResponseEntity<Response> addToCart(@RequestParam String bookName,@RequestHeader String token){
		return userService.addBook(bookName,token);
	}
	
	@GetMapping("/placeorder")
	public ResponseEntity<Response> placeOrder(@RequestHeader String token){
		return userService.buyBook(token);
	}
	
	@GetMapping("/showcart")
	public  Cart show(@RequestHeader String token) {
		return userService.showCart(token);
	}
	
	@DeleteMapping("/deleteuser")
	public String delete(@RequestHeader String token) {
		return userService.deleteUser(token);
	}
	
	@PutMapping("/updateuser")
	public ResponseEntity<Response> update(@RequestHeader String token, @RequestBody User user){
		return userService.updateUser(token, user);
	}
	
	@PutMapping("/increasecartvalue")
	public String increaseCart(@RequestHeader String token,@RequestParam String bookName) {
		return userService.increaseCartValue(token, bookName);
	}
	
	@PutMapping("/decreasecartvalue")
	public String decreaseCart(@RequestHeader String token,@RequestParam String bookName) {
		return userService.decreaseCartValue(token, bookName);
	}
	
	@DeleteMapping("/removefromcart")
	public ResponseEntity<Response> emptyCart(@RequestHeader String token,@RequestParam String bookName){
		return userService.removeFromCart(token, bookName);
	}
	
	@GetMapping("/showorderhistory")
	public  ResponseEntity<Response> showHistory(@RequestHeader String token){
		return userService.showOrderHistory(token);
	}
	
	@GetMapping("/pricelowtohigh")
	public List<Book> showBooksLowtoHigh(){
		return userService.showBookLowtoHigh();
	}
	
	@GetMapping("/newtooldbook")
	public List<Book> showBooksNewtoOld(){
		return userService.showBookNewtoOld();
	}
	
	@GetMapping("/findbookbyauthor")
	public ResponseEntity<Response> findBookByAuthor(@RequestParam String author) {
		return userService.findByAuthor(author);
	}

}
