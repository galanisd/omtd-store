package eu.openminted.store.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.openminted.store.common.OMTDStoreHandler;
import eu.openminted.store.common.StoreResponse;

@Component
public class OMTDStoreHandlerDefault implements OMTDStoreHandler{

	private static final Logger log = LoggerFactory.getLogger(OMTDStoreHandlerDefault.class);

    private final StoreService storeService;

    /**
     * Constructor.
     * @param storeService
     */
    @Autowired
    public OMTDStoreHandlerDefault(StoreService storeService) {
       this.storeService = storeService;
    }

	@Override
	public StoreResponse listFiles() {
    	String response = storeService.listAllFiles();
    	return new StoreResponse(response, "");
	}

	@Override
	public List<String> listFiles(String archiveId, boolean listDirectories, boolean recursive, boolean ignoreZips) {
		return storeService.listFiles(archiveId, listDirectories, recursive, ignoreZips);
	}

	@Override
	public List<String> listFiles(String archiveId, int from, int size) {
		return storeService.listFiles(archiveId, from, size);
	}

	@Override
	public StoreResponse deleteAll() {
    	String response = String.valueOf(storeService.deleteAll());
    	return new StoreResponse(response, "");
	}

	@Override
	public StoreResponse createArchive() {
    	String response = String.valueOf(storeService.createArchive());
    	return new StoreResponse(response, "");
	}

	@Override
	public StoreResponse deleteArchive(String archiveID) {
    	String response = String.valueOf(storeService.deleteArchive(archiveID, true));
    	return new StoreResponse(response, "");
	}

	@Override
	public StoreResponse createSubArchive(String archiveID, String archive) {
    	String response = storeService.createArchive(archiveID, archive);
    	return new StoreResponse(response, "");
	}

	@Override
	public StoreResponse finalizeArchive(String archiveID) {
    	String response = String.valueOf(storeService.finalizeArchive(archiveID));
    	return new StoreResponse(response, "");
	}

	@Override
	public StoreResponse storeFile(File file, String archiveID, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse downloadFile(String fileName, String destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse downloadArchive(String archiveID, String localDestination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse fetchMetadata(String archiveID, String localDestination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse createArchive(String archiveId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse archiveExists(String archiveId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse fileExistsInArchive(String archiveId, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse storeFile(byte[] bytes, String archiveID, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse deleteFile(String archiveID, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}


}
