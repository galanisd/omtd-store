package eu.openminted.store.common;

import java.io.File;


public interface OMTDStoreHandler {
	
	/**
	 * List all files of the Store.
	 * @return
	 */
	public StoreResponse listFiles();

	/**
	 * Delete all files in the Store.
	 * @return
	 */
	public StoreResponse deleteAll();
	
	/**
	 * @param archiveId
	 * @return
	 */
	public StoreResponse createArchive(String archiveId);
	
	/**
	 * Creates an archive
	 * @return the id of the archive.
	 */
	public StoreResponse createArchive();

	/**
	 * Deletes an archive.
	 * @return the id of the archive.
	 */
	public StoreResponse deleteArchive(String archiveID);
			
	/**
	 * Creates an subarchive
	 * @return the id of the subarchive.
	 */
	public StoreResponse createSubArchive(String archiveID, String archive);
	
	/**
	 * Finalize archive.
	 * @return the id of the archive.
	 */
	public StoreResponse finalizeArchive(String archiveID);
		
	/**
	 * Uploads a file in a archive.
	 * @param file
	 * @param archiveID
	 * @param fileName
	 * @return
	 */
	public StoreResponse storeFile(File file, String archiveID, String fileName);
	
	/**
	 * Uploads a file in a archive.
	 * @param bytes
	 * @param archiveID
	 * @param fileName
	 * @return
	 */
	public StoreResponse storeFile(byte[] bytes, String archiveID, String fileName);
	
	/**
	 * Downloads a file.
	 * @param fileName
	 * @param destination
	 * @return
	 */
	public StoreResponse downloadFile(String fileName, String destination);
	
	/**
	 * Deletes a file.
	 * @param fileName
	 * @param destination
	 * @return
	 */
	public StoreResponse deleteFile(String archiveID, String fileName);
	
	/**
	 * Downloads a file.
	 * @param fileName
	 * @param localDestination
	 * @return
	 */
	public StoreResponse downloadArchive(String archiveID, String localDestination);	
    
	/**
	 * @param archiveId
	 * @return
	 */
	public StoreResponse archiveExists(String archiveId);
	    
	/**
	 * @param archiveId
	 * @param fileName
	 * @return
	 */
	public StoreResponse fileExistsInArchive(String archiveId, String fileName);
}
