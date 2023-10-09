package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Book;

public interface IBookStoreRepo extends JpaRepository<Book,Integer>{
	 
	public Book findByBookName(String bookName);
	
	public Book findByAuthor(String author);
	
}
