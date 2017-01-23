package eu.openminted.store.restclient.cmd;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.store.restclient.StoreRESTClient;

/**
 * @author galanisd
 *
 */
public class RestClientTester {
	
	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);
	
	private StoreRESTClient store;
	
	@Autowired
	public RestClientTester(StoreRESTClient store) {
		super();
		this.store = store;
	}
	
	/**
	 * Run all tests.
	 * @param testPropertiesList
	 */
	public void runAllTests(TestPropertiesList testPropertiesList){
		this.test01(testPropertiesList.getMap().get("test01"));
	}
	
	/**
	 * Scenario 01.
	 * @param props
	 */
	public void test01(Properties props){					
		String result = store.deleteAll();
		log.info("Delete all" + result);		
		String archiveID = store.createArchive();
		log.info("Create Archive:" + archiveID);		
		String subArchiveId = store.createSubArchive(archiveID, props.getProperty("client.subarchiveid"));
		log.info("Create SubArchive:" + subArchiveId);		
		File fileForUploading = new File(props.getProperty("client.bigFile"));		
		store.updload(fileForUploading, subArchiveId, fileForUploading.getName());
		log.info("Upload to SubArchive:" + subArchiveId);			
		log.info("List Files");
		log.info(store.listFiles());		
		log.info("Download " + subArchiveId + "/" + fileForUploading.getName());
		boolean status = store.downloadFile(subArchiveId + "/" + fileForUploading.getName(), props.getProperty("client.downloadFolder") + fileForUploading.getName());
		log.info("Download status " + status);		
		log.info("Finalize archive " + archiveID + " " + store.finalizeArchive(archiveID));
		log.info("Download archive " + store.downloadArchive(archiveID, props.getProperty("client.downloadFolder") + archiveID + ".zip"));
	}
			
}
