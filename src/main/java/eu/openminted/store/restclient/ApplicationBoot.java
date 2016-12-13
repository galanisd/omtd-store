package eu.openminted.store.restclient;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author galanisd
 *
 */
public class ApplicationBoot {
	
	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);
	
	public static void main(String args[]){
		
		StoreRESTClient client = new StoreRESTClient("http://localhost:8080/");
		
		String result = client.deleteAll();
		log.info("Delete all" + result);
		
		String archiveID = client.createArchive();
		log.info("Create Archive:" + archiveID);
						
		File f = new File("C:/Users/galanisd/Desktop/Data/_AppTestData/DataForUpload/BILI_TMX_886_el-en.txt.zip"); 
		client.updload(f, archiveID, f.getName());
				
		log.info("List Files");
		log.info(client.listFiles());
		
	}
	
}
