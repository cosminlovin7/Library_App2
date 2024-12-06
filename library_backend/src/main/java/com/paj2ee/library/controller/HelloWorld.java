package com.paj2ee.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}

	@GetMapping("/hello-admin")
	public String helloAdmin() {
		return "Hello Admin";
	}
}
