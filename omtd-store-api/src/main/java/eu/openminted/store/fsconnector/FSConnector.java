package eu.openminted.store.fsconnector;

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
	
	/** Delete Folder.
	 * @param fileName
	 * @return
	 */
	boolean deleteFolder(String folder, boolean recursively);	
	
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
	
	/**
	 * Compress dir (recursively) and create a zip.
	 * @param dir
	 * @param zipFile
	 * @return
	 */
	public boolean compressDir(String dir, String zipFile);
	
	/**
	 * @param path
	 * @return
	 */
	public boolean isDir(String path);
	
	/**
	 * @param path
	 * @return
	 */
	public boolean exists(String path);
}
