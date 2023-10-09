package com.example.demo.exception;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.example.demo.dto.Response;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandling {
	
//	@Autowired
//	Response response;

	@ExceptionHandler(value= UserDataException.class)
	public ResponseEntity<String> findException(UserDataException exception) {
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.valueOf(exception.getStatusCode()));
	}
	
	@ExceptionHandler(value= AdminException.class)
	public ResponseEntity<String> findAdminException(AdminException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.valueOf(exception.getStatusCode()));
	}
	

}
