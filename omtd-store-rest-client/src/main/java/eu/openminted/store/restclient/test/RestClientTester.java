package eu.openminted.store.restclient.test;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.store.restclient.StoreRESTClient;
import eu.openminted.store.restclient.cmd.ApplicationBoot;

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
	 * Scenario 01.
	 * @param props
	 */
	public void test01(Properties props){					
		String result = store.deleteAll().getResponse();
		log.info("Delete all" + result);		
		String archiveID = store.createArchive().getResponse();
		log.info("Create Archive:" + archiveID);		
		String subArchiveId = store.createSubArchive(archiveID, props.getProperty("client.subarchiveid")).getResponse();
		log.info("Create SubArchive:" + subArchiveId);		
		File fileForUploading = new File(props.getProperty("client.bigFile"));		
		String status1 = store.updload(fileForUploading, subArchiveId, fileForUploading.getName()).getResponse();
		log.info("Upload to SubArchive:" + status1);			
		log.info("List Files");
		log.info(store.listFiles().getResponse());		
		log.info("Download " + subArchiveId + "/" + fileForUploading.getName());
		String status2 = store.downloadFile(subArchiveId + "/" + fileForUploading.getName(), props.getProperty("client.downloadFolder") + fileForUploading.getName()).getResponse();
		log.info("Download status " + status2);		
		log.info("Finalize archive " + archiveID + " " + store.finalizeArchive(archiveID).getResponse());
		log.info("Download archive " + store.downloadArchive(archiveID, props.getProperty("client.downloadFolder") + archiveID + ".zip"));
	}
			
}
