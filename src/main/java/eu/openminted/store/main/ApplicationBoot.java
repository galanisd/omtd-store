package eu.openminted.store.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"eu.openminted.storage.controller", "eu.openminted.storage.config"})
public class ApplicationBoot {
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationBoot.class, args);		
	}
}
