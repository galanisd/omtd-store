package eu.openminted.store.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public interface OMTDStoreHandler {
	
	/**
	 * List all files of the Store.
	 * @return
	 */
	public StoreResponse listFiles();

	/**
	 * List all files in a specific Archive.
     * @param archiveId
	 * @param listDirectories
	 * @param recursive
	 * @param ignoreZips
	 * @return {@link List<String>}
	 */
	public List<String> listFiles(String archiveId, boolean listDirectories, boolean recursive, boolean ignoreZips);

	/**
	 * List {@param size} files in a specific Archive starting from {@param from}.
     * @param archiveId
     * @param from
     * @param size
	 * @return
	 */
	public List<String> listFiles(String archiveId, int from, int size);

	/**
	 * Retrieves the total size of all the contents of an Archive.
     * @param archiveId
	 * @return
	 */
	long getSize(String archiveId);

	/**
	 * Retrieves the size of an Archive.
     * @param archiveId
	 * @return
	 */
	long getSizeOnDisk(String archiveId);

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
	 * Clone an existing archive
	 */
	public StoreResponse cloneArchive(String archiveId);
	
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
	 * @param archiveID
	 * @param fileName
	 * @return
	 */
	public StoreResponse deleteFile(String archiveID, String fileName);

	/**
	 * Moves a file.
	 * @param archiveID
	 * @param fileName
	 * @param moveTo
	 * @return
	 */
	public StoreResponse moveFile(String archiveID, String fileName, String moveTo);
	
	/**
	 * Downloads a file.
	 * @param archiveID
	 * @param localDestination
	 * @return
	 */
	public StoreResponse downloadArchive(String archiveID, String localDestination);

	/**
	 * Downloads as zip the metadata of an archive.
	 * @param archiveID
	 * @param localDestination
	 * @return
	 */
	public StoreResponse fetchMetadata(String archiveID, String localDestination);

	/**
	 * Downloads as zip the annotations of an archive.
	 * @param archiveID
	 * @param localDestination
	 * @return
	 */
	public StoreResponse fetchAnnotations(String archiveID, String localDestination);
    
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
