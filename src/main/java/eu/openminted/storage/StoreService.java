package eu.openminted.storage;

import java.io.InputStream;

/**
 * The Store Service of OpenMinTeD.
 * Created by antleb on 11/9/16.
 */
public interface StoreService {

    /**
     * Initializes a new empty archive
     * 
     * @return the identifier of the archive
     */
    String createArchive();

    /**
     * Initializes a new empty archive contained in the parent archive
     * 
     * @param parent The parent archive id
     * @param name the name of the new archive
     *
     * @return the identifier of the archive
     * 
     */
    String createArchive(String parentArchiveId, String name);
    
    /**
     * Deletes an archive.
     * @param archiveId
     * @param force
     */
    boolean deleteArchive(String archiveId, boolean force);

    /**
     * Downloads the contents of the archive in a zip bundle.
     * 
     * @param archiveId the identifier of the archive to download.
     *                  
     * @return an input stream with the contents of the archive.
     */
    InputStream downloadArchive(String archiveId);
        
    
    /**
     * Get archive info for an archive 
     * @param archiveId
     * @return
     */
    ArchiveInfo getArchiveInfo(String archiveId);
    
    /**
     * Stores the contents of the input stream in the archive as a file.
     * 
     * @param archiveId the archive to store the file to
     * @param is the contents of the file
     * @param fileName (optional) the name of the file. Random UUID, if not specified.
     *           
     * @return true if the files was stored.
     */    
    boolean storeFile(String archiveId, InputStream is, String fileName);
        
    /**
     * Downloads file. 
     * @param fileName is the path of the file.
     * @return
     */
    InputStream downloadFile(String fileName);
    
    /**
     * Deletes all files.
     * @param fileName
     */
    boolean deleteAll();
    
    /**
     * Deletes a file.
     * @param fileName
     */
    boolean deleteFile(String archiveId, String fileName);
    
    /**
     * Lists all files.
     * @param fileName
     */
    public String listAllFiles();
      
}

