package com.example.demo.utility;

import java.util.HashMap;
import org.springframework.stereotype.Component;
import com.example.demo.dto.LoginDto;
import com.example.demo.exception.UserDataException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtility {
	
	String secretkey = "pass@123";

	public String generateToken(String email, String Password) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("password", Password);
		String key = Jwts.builder().setClaims(map).signWith(SignatureAlgorithm.HS256, secretkey).compact();
		return key;
	}
	
	public LoginDto decodeToken(String token) {

		try {
			Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
		} catch (Exception e) {
			throw new UserDataException("Invalid token",401);
		}
		Claims decodedToken = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
		LoginDto credentials = new LoginDto();

		credentials.setEmail((String) (decodedToken.get("email")));
		credentials.setPassword((String) (decodedToken.get("password")));

		return credentials;

	}



}
