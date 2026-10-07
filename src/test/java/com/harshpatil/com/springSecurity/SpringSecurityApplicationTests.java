package com.harshpatil.com.springSecurity;

import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		User user=new User(4L,"harsh@gmail.com","1234");

		String token=jwtService.generateToken(user);
		System.out.println(token);

		Long id=jwtService.getUserIdFromToken(token);
		System.out.println(id);
	}

}
