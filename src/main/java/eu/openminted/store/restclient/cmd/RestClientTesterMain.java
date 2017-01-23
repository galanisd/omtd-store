package eu.openminted.store.restclient.cmd;

import eu.openminted.store.restclient.StoreRESTClient;

public class RestClientTesterMain {
	
	public static void main(String args[]){
		
		// Create a  RestClientTester
		StoreRESTClient store = new StoreRESTClient("http://localhost:8080/"); 
		RestClientTester restClientTester = new RestClientTester(store);
		
		// Test config.
		String [] names = {"test01"};
		TestPropertiesList testPropsList = new TestPropertiesList();
		testPropsList.loadTestFileProps(names);
		
		// Test
		restClientTester.runAllTests(testPropsList);
	}
}
