package com.example.demo.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.Response;
import com.example.demo.exception.UserDataException;
import com.example.demo.model.Book;
import com.example.demo.model.Cart;
import com.example.demo.model.CartBook;
import com.example.demo.model.Orders;
import com.example.demo.model.User;
import com.example.demo.repository.CartBookRepo;
import com.example.demo.repository.IBookStoreRepo;
import com.example.demo.repository.IOrderRepo;
import com.example.demo.repository.IUserDataRepo;
import com.example.demo.utility.JWTUtility;
import com.example.demo.utility.NewMailSender;

@Service
public class UserService implements IUserService {

	@Autowired
	IUserDataRepo userRepo;

	@Autowired
	IBookStoreRepo bookRepo;

	@Autowired
	Response response;

	@Autowired
	JWTUtility jwt;

	@Autowired
	CartBookRepo cartBookRepo;

	@Autowired
	IOrderRepo orderRepo;
	
	@Autowired
	NewMailSender mailSender;

	@Override
	public ResponseEntity<Response> addUser(User user) {
		if ((userRepo.findByEmailAndPassword(user.getEmail(), user.getPassword())) == null) {
			userRepo.save(user);
			response.setObject(user);
			response.setMsg("You have registered successfully into BookStore");
			mailSender.sendMail(user.getEmail(),"Regestration Successful",user.getFullName()+"You Have Successfully regisetred into BookStore");
			return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
		} else {
			throw new UserDataException("User registerd already", 404);
		}
	}

	@Override
	public String login(String email, String password) {
		User user = userRepo.findByEmailAndPassword(email, password);
		if (user != null) {
			String token = jwt.generateToken(email, password);
			return "You have loggged in successfully   " + token;
		} else {
			throw new UserDataException("Login Failed Invalid email or password", 404);
		}
	}

	@Override
	public ResponseEntity<Response> addBook(String bookName, String token) {
		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		Book book = bookRepo.findByBookName(bookName);

		if (book == null) {
			throw new UserDataException("Invalid Book Name", 404);
		}

		if (bookRepo.findByBookName(bookName).getQuantity() == 0) {
			throw new UserDataException("Book is out of stock", 404);
		}
		Cart cart = user.getCart();

		List<CartBook> bookList = cart.getBooklist();

		for (CartBook bookCheck : bookList) {
			if (bookCheck.getBookName().equals(bookName)) {
				throw new UserDataException("Book is already added to cart", 404);
			}
		}

		CartBook cartBook = new CartBook();
		cartBook.setBookName(bookName);
		cartBook.setBookPrice(book.getPrice());
		cartBook.setBookQuantity(1);

		bookList.add(cartBook);

		cart.setBooklist(bookList);

		int totalPrice = cart.getTotalPrice();
		totalPrice = totalPrice + book.getPrice();
		cart.setTotalPrice(totalPrice);

		user.setCart(cart);

		userRepo.save(user);

		response.setObject(cartBook);
		response.setMsg("Book added successfully to cart");

		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);

	}

	@Override
	public ResponseEntity<Response> buyBook(String token) {
		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		Cart cart = user.getCart();
		if (cart.getTotalPrice() == 0) {
			throw new UserDataException("Your cart is empty", 404);
		}

		List<CartBook> bookList = cart.getBooklist();

		for (CartBook cartBook : bookList) {
			Book book = bookRepo.findByBookName(cartBook.getBookName());
			book.setQuantity(book.getQuantity() - cartBook.getBookQuantity());
			bookRepo.save(book);
		}

		Orders order = new Orders();

		order.setOrderList(bookList);
		order.setTotalPrice(cart.getTotalPrice());

		List<Orders> orderList = user.getOrder();

		orderList.add(order);

		user.setOrder(orderList);

		userRepo.save(user);

		for (CartBook cartBook : bookList) {
			cartBookRepo.deleteById(cartBook.getCartBookId());
		}

		cart.setTotalPrice(0);
		cart.setBooklist(null);

		user.setCart(cart);

		userRepo.save(user);
		
		mailSender.sendMail(user.getEmail(),"Order Plcaed Successfully","Your order is dispalced for "+user.getOrder());

		response.setObject(order);
		response.setMsg("Your Order is Placed Successfully");

		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	public Cart showCart(String token) {
		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		return user.getCart();

	}

	@Override
	public String deleteUser(String token) {

		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		userRepo.delete(user);

		return "User deleted successfully ";
	}

	@Override
	public ResponseEntity<Response> updateUser(String token, User user) {
		LoginDto credentials = jwt.decodeToken(token);
		User user1 = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user1 == null)) {
			throw new UserDataException("Invalid token", 404);
		}
		user.setUserId(user1.getUserId());
		userRepo.save(user);
		response.setObject(user);
		response.setMsg("User Updated Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public String increaseCartValue(String token, String bookName) {

		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		Book book = bookRepo.findByBookName(bookName);
		if (book == null) {
			throw new UserDataException("Invalid Book Name", 404);
		}

		if (book.getQuantity() == 0) {
			throw new UserDataException("Book is out of stock", 404);
		}

		List<CartBook> bookList = user.getCart().getBooklist();

		Cart cart = user.getCart();

		int bookIndex = 0;
		int bookCount = 0;
		CartBook cartBook1 = new CartBook();

		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getBookName().equals(bookName)) {
				bookIndex = i;
				bookCount = 1;
				cartBook1 = bookList.get(i);
			}

		}

		if (bookCount == 0) {
			throw new UserDataException("Book is not added to cart add to cart first", 404);
		}

		cartBook1.setBookQuantity(cartBook1.getBookQuantity() + 1);
		bookList.set(bookIndex, cartBook1);
		int cartPrice = cart.getTotalPrice();
		cartPrice += cartBook1.getBookPrice();
		cart.setBooklist(bookList);
		cart.setTotalPrice(cartPrice);
		user.setCart(cart);
		userRepo.save(user);

		return "Cart value is increased";
	}

	@Override
	public String decreaseCartValue(String token, String bookName) {

		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		Book book = bookRepo.findByBookName(bookName);
		if (book == null) {
			throw new UserDataException("Invalid Book Name", 404);
		}

		Cart cart = user.getCart();
		List<CartBook> bookList = cart.getBooklist();

		int bookIndex = 0;
		int bookCount = 0;

		CartBook cartBook1 = new CartBook();

		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getBookName().equals(bookName)) {
				bookIndex = i;
				bookCount = 1;
				cartBook1 = bookList.get(i);
			}

		}

		if (bookCount == 0) {
			throw new UserDataException("Book is not in the cart", 404);
		}

		if (cartBook1.getBookQuantity() == 0) {
			throw new UserDataException("Book is not in the cart", 404);
		}

		cartBook1.setBookQuantity(cartBook1.getBookQuantity() - 1);

		int totalPrice = cart.getTotalPrice();
		totalPrice -= cartBook1.getBookPrice();
		
		if (cartBook1.getBookQuantity() == 0) {
			bookList.remove(bookIndex);

		}
		else {
			bookList.set(bookIndex, cartBook1);
		}

		cart.setTotalPrice(totalPrice);
		cart.setBooklist(bookList);
		user.setCart(cart);
		userRepo.save(user);

		return "Book quantity reduced by one";
	}

	@Override
	public ResponseEntity<Response> removeFromCart(String token, String bookName) {

		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}

		Book book = bookRepo.findByBookName(bookName);
		if (book == null) {
			throw new UserDataException("Invalid Book Name", 404);
		}

		Cart cart = user.getCart();
		List<CartBook> bookList = cart.getBooklist();

		int bookIndex = 0;
		int bookCount = 0;

		CartBook cartBook1 = new CartBook();
		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getBookName().equals(bookName)) {
				bookIndex = i;
				bookCount = 1;
				cartBook1 = bookList.get(i);

			}

		}

		if (bookCount == 0) {
			throw new UserDataException("Book is not in the cart", 404);
		}

//		 CartBook bookFromCart = bookList.get(bookIndex);

		int cartPrice = cart.getTotalPrice() - (cartBook1.getBookPrice() * cartBook1.getBookQuantity());

		cart.setTotalPrice(cartPrice);
		bookList.remove(bookIndex);
		cart.setBooklist(bookList);

		user.setCart(cart);
		userRepo.save(user);

		response.setMsg("Book removed from cart");
		response.setObject(cartBook1);

		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<Response> showOrderHistory(String token) {
		LoginDto credentials = jwt.decodeToken(token);
		User user = userRepo.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());

		if ((user == null)) {
			throw new UserDataException("Invalid token", 404);
		}
		response.setObject(user.getOrder());
		response.setMsg("Your Previous order");
		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public List<Book> getAllBooks() {

		return bookRepo.findAll();
	}

	@Override
	public List<Book> showBookLowtoHigh() {
		
		return bookRepo.findAll(Sort.by(Direction.ASC,"price"));
	}

	@Override
	public List<Book> showBookNewtoOld() {
		
		return bookRepo.findAll(Sort.by(Direction.DESC,"bookId"));
	}

	@Override
	public ResponseEntity<Response> findByAuthor(String author) {
		Book book  = bookRepo.findByAuthor(author);
		if (book != null) {
			response.setObject(book);
			response.setMsg("Found book by given Author name");
			return new ResponseEntity<Response>(response,HttpStatus.ACCEPTED);
		}
		else {
			throw new UserDataException("Invalid Author Name",404);
		}
	}

}
