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
	public boolean makeFolder(String file);
	
	/**
	 * Store file.
	 * @param file
	 * @param is
	 * @return
	 */
	public boolean storeFile(String targetFileName, InputStream is);
		
	
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
}
