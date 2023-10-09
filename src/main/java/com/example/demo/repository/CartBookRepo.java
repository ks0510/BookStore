package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CartBook;

public interface CartBookRepo extends JpaRepository<CartBook,Integer>{

}
