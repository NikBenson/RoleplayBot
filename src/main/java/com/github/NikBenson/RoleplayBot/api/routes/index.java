package com.github.NikBenson.RoleplayBot.api.routes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test/")
public class index {
	@RequestMapping(value="/status/", method= RequestMethod.GET, produces = "application/json")
	public String status()
	{
		System.out.println("aaaaa");
		return "online";
	}

	@RequestMapping("/")
	public String index() {
		System.out.println("aaaaa");
		return "Greetings from Spring Boot!";
	}
}
