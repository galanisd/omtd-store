package eu.openminted.store.restclient.test;

import eu.openminted.store.restclient.StoreRESTClient;

public class RestClientTesterMain {
	
	public static void main(String args[]){
		
		// Create a  RestClientTester
		StoreRESTClient store = new StoreRESTClient("http://localhost:8080/"); 
		RestClientTester restClientTester = new RestClientTester(store);
		
		// Run tests.
		restClientTester.test01(TestProperties.loadProps());
		
	}
}
