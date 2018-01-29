package eu.openminted.store.fsconnector;


import java.io.InputStream;
import java.util.List;

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
     * @param fileName
	 */
	String listAllFiles();

	/**
	 * Lists files inside {@param fileName}.
	 * @param fileName
	 * @param listDirectories
	 * @param recursive
	 * @return {@link List<String>}
	 */
	List<String> listFiles(String fileName, boolean listDirectories, boolean recursive);

	/**
	 * List all files.
     * @param fileName
     * @param from
     * @param size
	 * @return {@link List<String>}
	 */
	List<String> listFiles(String fileName, int from, int size);

//	/** //TODO: remove
//	 * List all files.
//	 * @return
//     * @param corpusId
//     * @param from
//     * @param size
//	 */
//	ArrayList<Publication> listCorpus(String corpusId, int from, int size);
	
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
