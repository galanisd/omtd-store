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
		
		String result = store.deleteAll();
		log.info("Delete all" + result);		
		String archiveID = store.createArchive();
		log.info("Create Archive:" + archiveID);		
		String subArchiveId = store.createSubArchive(archiveID, "authors");
		log.info("Create SubArchive:" + subArchiveId);		
		File fileForUploading = new File("C:/Users/galanisd/Desktop/Data/OpenAIRE/openairemetadata/stelios_metadata/authors.json");		
		store.updload(fileForUploading, subArchiveId, fileForUploading.getName());
		log.info("Upload to SubArchive:" + subArchiveId);			
		log.info("List Files");
		log.info(store.listFiles());		
		log.info("Download " + subArchiveId + "/" + fileForUploading.getName());
		boolean status = store.downloadFile(subArchiveId + "/" + fileForUploading.getName(), "C:/Users/galanisd/Desktop/Data/_AppTestData/Downloaded/" + fileForUploading.getName());
		log.info("Download status " + status);
		
		log.info("finalize archive " + archiveID + " " + store.finalizeArchive(archiveID));
		log.info("dowload archive " + store.downloadArchive(archiveID, "C:/Users/galanisd/Desktop/Data/_AppTestData/Downloaded/" + archiveID + ".zip"));
		
		
	}
	
}
