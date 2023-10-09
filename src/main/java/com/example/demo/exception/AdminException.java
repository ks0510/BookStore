package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminException extends RuntimeException  {
	
	private String message;
	private int statusCode;

}
