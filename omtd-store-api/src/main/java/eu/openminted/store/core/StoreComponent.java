package eu.openminted.store.core;

import java.io.File;

import eu.openminted.store.common.OMTDStoreHandler;
import eu.openminted.store.common.StoreResponse;

public class StoreComponent implements OMTDStoreHandler{

	@Override
	public StoreResponse listFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse deleteAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse createArchive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse deleteArchive(String archiveID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse createSubArchive(String archiveID, String archive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse finalizeArchive(String archiveID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoreResponse updload(File file, String archiveID, String fileName) {
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

}
