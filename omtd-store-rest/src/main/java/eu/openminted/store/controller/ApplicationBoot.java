package eu.openminted.store.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"eu.openminted.store.controller", "eu.openminted.store.config", "eu.openminted.store"})
public class ApplicationBoot {	
	public static void main(String args[]){
	}
}
