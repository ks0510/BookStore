package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.model.Book;
//import com.example.demo.Customer;
import com.example.demo.dto.Response;
import com.example.demo.exception.AdminException;
import com.example.demo.repository.IBookStoreRepo;

@Service
public class BookStoreService implements IBookStoreService {

	@Autowired
	IBookStoreRepo repo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	Response response;

	@Override
	public ResponseEntity<Response> add(Book book) {
		
		if (repo.findByBookName(book.getBookName()) == null) {
			repo.save(book);
			response.setObject(book);
			response.setMsg("Book added successfully");
			return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
		}
		else {
			throw new AdminException("Book already added to the store",404);
		}

	}

	@Override
	public ResponseEntity<Response> updatePrice(int bookId, int price) {
		Optional<Book> book = repo.findById(bookId);
		book.get().setPrice(price);
		repo.save(book.get());
		response.setObject(book);
		response.setMsg("Price Updated Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public String delete(int id) {
		repo.deleteById(id);
		return "Book deleted successfully";
	}

	@Override
	public ResponseEntity<Response> updateQuantity(int id, int quantity) {
		Optional<Book> book = repo.findById(id);
		book.get().setQuantity(quantity);
		repo.save(book.get());
		response.setObject(book);
		response.setMsg("Quantity Updated Successfully");
		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public List<Book> getAllBooks() {
		return repo.findAll();
	}

}
