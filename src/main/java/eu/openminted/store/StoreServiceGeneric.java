package eu.openminted.store;

import java.io.InputStream;

import eu.openminted.store.config.StoreProperties;
import eu.openminted.store.fsconnector.FSConnector;
import eu.openminted.store.fsconnector.FSConnectorBuilder;
import eu.openminted.store.idgenerator.IdGenerator;
import eu.openminted.store.index.StoreIndex;

/**
 * 
 * @author galanisd
 *
 */
public class StoreServiceGeneric implements StoreService{
		
	private String type;
	protected StoreProperties storageProperties;
	protected IdGenerator idGen;
	protected StoreIndex storageIndex;
	private FSConnector connector;
	
	/**
	 * Constructor.
	 * @param type
	 * @param storageProperties
	 * @param idGen
	 * @param storageIndex
	 */
	public StoreServiceGeneric(String type, StoreProperties storageProperties, IdGenerator idGen, StoreIndex storageIndex) {
		super();
		connector = FSConnectorBuilder.getConnector(type, storageProperties);				
		this.type = type;
		this.storageProperties = storageProperties;
		this.idGen = idGen;
		this.storageIndex = storageIndex;
		
	}
	
	public IdGenerator getIdGen() {
		return idGen;
	}
	
	public void setIdGen(IdGenerator idGen) {
		this.idGen = idGen;
	}
	
	public StoreProperties getStorageProperties() {
		return storageProperties;
	}

	public void setStorageProperties(StoreProperties storageProperties) {
		this.storageProperties = storageProperties;
	}

	public StoreIndex getStorageIndex() {
		return storageIndex;
	}

	public void setStorageIndex(StoreIndex storageIndex) {
		this.storageIndex = storageIndex;
	}
	
	@Override
	public String createArchive() {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);
		
		// Get new archive id.
		String archiveId = idGen.generateId();
		// Create Folder.
		String destinationFolderAbsolutePath = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), archiveId);
		System.out.println(destinationFolderAbsolutePath.toString());
		boolean creationStatus = connector.makeFolder(destinationFolderAbsolutePath);
		// Return archiveId.
    	if(creationStatus){    		
    		return archiveId;
    	}else{
    		return null;
    	}
	}

	@Override
	public String createArchive(String parentArchiveId, String name) {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);
		
		// Get new archive id.
		String archiveId = idGen.generateId();
		// Create Folder.
		String destinationFolderAbsolutePathForParent = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), parentArchiveId);
		String destinationFolderAbsolutePath = Helper.appendDirToPath(destinationFolderAbsolutePathForParent, archiveId);		
		System.out.println(destinationFolderAbsolutePath.toString());
		boolean creationStatus = connector.makeFolder(destinationFolderAbsolutePath);
		// Return archiveId.
    	if(creationStatus){
    		storageIndex.addChildArchive(parentArchiveId, archiveId);
    		return archiveId;    		
    	}else{
    		return null;
    	}
	}

	@Override
	public boolean deleteArchive(String archiveId, boolean force) {
		String destinationFolderAbsolutePath = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), archiveId);
		return connector.deleteFolder(destinationFolderAbsolutePath, force);
		
	}

	@Override
	public InputStream downloadArchive(String archiveId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArchiveInfo getArchiveInfo(String archiveId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeFile(String archiveId, InputStream is, String fileName) {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);
		String destinationFolderAbsolutePathForParent = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), archiveId);
		String destinationFile = Helper.appendFileToPath(destinationFolderAbsolutePathForParent, fileName);
		return connector.storeFile(destinationFile, is);
		
	}

	@Override
	public boolean deleteFile(String archiveId, String fileName) {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);
		String destinationFolderAbsolutePathForParent = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), archiveId);
		String destinationFile = Helper.appendFileToPath(destinationFolderAbsolutePathForParent, fileName);
		return connector.deleteFile(destinationFile);
					
	}

	@Override
	public boolean deleteAll() {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);
		return connector.deleteAll();
	}
	
	@Override
	public String listAllFiles() {
		//FSConnector connector = FSConnectorBuilder.getConnector(type, storageProperties);		
		return connector.listAllFiles();
	}

	@Override
	public InputStream downloadFile(String fileName) {
		String destinationFile = storageProperties.getStorageRoot() + fileName;
		return connector.download(destinationFile);
	}


}
