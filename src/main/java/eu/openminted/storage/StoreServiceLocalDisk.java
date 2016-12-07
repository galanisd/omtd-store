package eu.openminted.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import eu.openminted.storage.config.Storage;
import eu.openminted.storage.fsconnector.FSConnectorBuilder;
import eu.openminted.storage.index.StorageIndex;

/**
 * An {@link eu.openminted.storage.StoreService} implementation for local disk.
 * @author galanisd
 *
 */
public class StoreServiceLocalDisk extends StoreServiceGeneric{
		
	/**
	 * Constructor.
	 * @param storageProperties
	 * @param idGen
	 * @param storageIndex
	 */
	@Autowired
	public StoreServiceLocalDisk(StorageProperties storageProperties, IdGenerator idGen, StorageIndex storageIndex){		
		super(Storage.LOCAL, storageProperties, idGen, storageIndex);		 
	}		

	@Override
	public boolean deleteArchive(String archiveId, boolean force) {
		
		String destinationFolderAbsolutePath = Helper.getAbsolutePathForArchive(storageIndex, storageProperties.getStorageRoot(), archiveId);
				
		if(force){
			/*
			try {
				Files.walk(destinationFolderAbsolutePath)
			      .map(Path::toFile)
			      .sorted( Comparator.comparing( File::isDirectory ) ) 
			      .forEach( File::delete );
			} catch (IOException e) {
				
			}
			*/
			return false;
		}else{
			return Paths.get(destinationFolderAbsolutePath).toFile().delete();			
		}
	}
	
}
