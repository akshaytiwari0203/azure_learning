package com.learning.controller;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	
	@RequestMapping("/{name}")
	public String greet(@PathVariable("name") String name) {
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.format("Hello %s!!!\nThis request was served by Host %s", name, hostAddress);
	}

}
