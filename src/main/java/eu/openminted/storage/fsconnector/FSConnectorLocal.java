package eu.openminted.storage.fsconnector;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorLocal implements FSConnector{

	public boolean makeFolder(String fileName){
    	// Make folder			
		Path destinationFolderAboslutePath = Paths.get(fileName);
    	boolean status = destinationFolderAboslutePath.toFile().mkdirs();    	
    	return status;
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {
		
		try{
			Path path = Paths.get(targetFileName);
			path.toFile().getParentFile().mkdirs();		
			Files.copy(is, path);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteFile(String fileName) {
		try{			
			Path path = Paths.get(fileName);
			return path.toFile().delete();
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String listAllFiles() {
		return "";
	}

	@Override
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		return false;
	}
}
