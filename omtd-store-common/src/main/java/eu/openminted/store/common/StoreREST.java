package eu.openminted.store.common;

/**
 * 
 * @author galanisd
 *
 */
public class StoreREST {
	
	// Endpoints
	public static final String createArchive = "/store/createArchive/";
	public static final String cloneArchive = "/store/cloneArchive/";
	public static final String createSubArchive = "/store/createSubArchive/";
	public static final String deleteArchive = "/store/deleteArchive/";
	public static final String finalizeArchive = "/store/finalizeArchive/";
	public static final String downloadArchive = "/store/downloadArchive/";

	public static final String fetchMetadata = "/store/metadata/fetchMetadata/";
	public static final String fetchAnnotations = "/store/annotations/fetchAnnotations/";

	public static final String getSize = "/store/getSize/";
	public static final String getSizeOnDisk = "/store/getSizeOnDisk/";

	public static final String listFiles = "/store/listFiles/";
	public static final String listFilesInArch = "/store/listFilesInArch/";
	public static final String listFilesPaged = "/store/listFilesPaged/";

	public static final String deleteAll = "/store/deleteAll/";
		
	public static final String uploadFile = "/store/uploadFile/";
	public static final String downloadFile = "/store/downloadFile/";
	public static final String deleteFile = "/store/deleteFile/";
		
	public static final String archiveExists = "/store/archiveExists/";
	public static final String fileExistsInArchive = "/store/fileExistsInArchive/";
	
	// Params.
	public static final String archiveID = "archiveID";
	public static final String listDirectories = "listDirectories";
	public static final String recursive = "recursive";
	public static final String ignoreZipFiles = "ignoreZipFiles";
	public static final String fileListIndex = "fileListIndex";
	public static final String fileListSize = "fileListSize";
	public static final String fileName = "fileName";
	public static final String file = "file";
}
