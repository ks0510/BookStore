package com.example.demo.dto;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class Response {
	
	Object object;
	String msg;

}
