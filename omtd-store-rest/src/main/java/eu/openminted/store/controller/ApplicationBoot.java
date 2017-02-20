package eu.openminted.store.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.core.StoreServiceGeneric;

@SpringBootApplication
//@ComponentScan(basePackages = {"eu.openminted.store.controller", "eu.openminted.store.config", "eu.openminted.store"})
@ComponentScan(basePackageClasses = {StoreController.class, ApplicationConfig.class, StoreServiceGeneric.class})
public class ApplicationBoot {	
	public static void main(String args[]){
	}
}
