package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.example.demo.*" })
@SpringBootApplication(
		exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
)
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
