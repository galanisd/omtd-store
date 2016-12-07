package eu.openminted.storage.fsconnector;

import java.io.InputStream;

/**
 * @author galanisd
 *
 */
public interface FSConnector {
	
	/**
	 * Make folder
	 * @param file
	 * @return
	 */
	boolean makeFolder(String file);
	
	/**
	 * Store file.
	 * @param file
	 * @param is
	 * @return
	 */
	boolean storeFile(String targetFileName, InputStream is);
	
		
	/** Delete All.
	 * @param fileName
	 * @return
	 */
	boolean deleteAll();
	
	/** Delete file.
	 * @param fileName
	 * @return
	 */
	boolean deleteFile(String fileName);	
	
	/**
	 * List all files.
	 * @return
	 */
	String listAllFiles();	
	
	/**
	 * Download.
	 * @param targetFileName
	 * @return
	 */
	public InputStream download(String targetFileName);
}
