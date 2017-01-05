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
		
		StoreRESTClient store = new StoreRESTClient("http://localhost:8080/");
		
		//String result = client.deleteAll();
		//log.info("Delete all" + result);
		
		String archiveID = store.createArchive();
		log.info("Create Archive:" + archiveID);
						
		//File f = new File("C:/Users/galanisd/Desktop/Data/_AppTestData/DataForUpload/BILI_TMX_886_el-en.txt.zip");
		File f = new File("C:/Users/galanisd/Desktop/Data/OpenAIRE/openairemetadata/stelios_metadata/authors.json");
		//File f = new File("C:/Users/galanisd/Desktop/Data/OpenAIRE/openairemetadata/stelios_metadata/authorspubs.json");
		
		store.updload(f, archiveID, f.getName());
				
		log.info("List Files");
		log.info(store.listFiles());
		
		log.info("Download");
		//store.download(archiveID + "/" + f.getName(), "C:/Users/galanisd/Desktop/Data/_AppTestData/Downloaded/" + f.getName());
		
	}
	
}
