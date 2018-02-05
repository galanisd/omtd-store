package eu.openminted.store.core;

import java.io.InputStream;
import java.util.List;

/**
 * The Store Service of OpenMinTeD.
 * Created by antleb on 11/9/16.
 */
public interface StoreService {

	/**
     * Initializes a new empty archive with archiveId as name.
     * 
     * @return the identifier of the archive
     */
    boolean createArchive(String archiveId);
    
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
     * Finalize archive.
     * @param archiveId
     * @return
     */
    boolean finalizeArchive(String archiveId);

    /**
     * Finalize archive.
     * @param archiveId
     * @return
     */
    InputStream fetchMetadata(String archiveId);
    
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
     * Stores the contents of the input stream to the root of the storage.
     * 
     * @param is the contents of the file
     * @param fileName (optional) the name of the file. Random UUID, if not specified.
     *           
     * @return true if the files was stored.
     */    
    boolean storeFile(InputStream is, String fileName);

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

    /**
     * Lists files inside {@param fileName}.
     * @param fileName
     * @param listDirectories
     * @param recursive
     * @return {@link List<String>}
     */
    public List<String> listFiles(String fileName, boolean listDirectories, boolean recursive);

    /**
     * Lists {@param size} files starting from {@param from}.
     * @param fileName
     * @param from
     * @param size
     * @return {@link List<String>}
     */
    public List<String> listFiles(String fileName, int from, int size);

//    /** //TODO: remove
//     * Lists Corpus.
//     * @param corpusId
//     * @param from
//     * @param size
//     */
//    public ArrayList<Publication> listCorpus(String corpusId, int from, int size); // FIXME: corpusId or archiveId?

    /**
     * Archive exists
     * @param archiveId
     */
    public boolean archiveExists(String archiveId);
    
    /**
     * File exists in archive.
     * @param archiveId
     */
    public boolean fileExistsInArchive(String archiveId, String fileName);
}

