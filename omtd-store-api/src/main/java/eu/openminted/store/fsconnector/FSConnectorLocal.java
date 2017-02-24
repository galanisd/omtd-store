package eu.openminted.store.fsconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorLocal implements FSConnector{
	
	private static final Logger log = LoggerFactory.getLogger(FSConnectorLocal.class);	
	private String localRoot;
		
	public FSConnectorLocal(String localRoot) {
		super();
		this.localRoot = localRoot;
	}

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
			log.debug("deleting->" + fileName);
			Path path = Paths.get(fileName);
			return path.toFile().delete();
		}catch(Exception e){
			log.debug("ERROR:", e);
			return false;
		}
	}

	@Override
	public String listAllFiles() {
		String list = "";
		File root = new File(localRoot);
		ArrayList<File> allFiles = listFileTree(root);
		for(File f : allFiles){
			list = list + f.getAbsolutePath().substring(this.localRoot.length()) + "\n";
		}
		
		return list;
	}

	public static ArrayList<File> listFileTree(File dir) {
		ArrayList<File> fileTree = new ArrayList<File>();
		
	    if(dir == null || dir.listFiles() == null){
	        return fileTree;
	    }
	    
	    for (File entry : dir.listFiles()) {
	        if (entry.isFile()) {
	        	fileTree.add(entry);
	        }else {
	        	fileTree.add(entry);
	        	fileTree.addAll(listFileTree(entry));
	        }
	    }
	    return fileTree;
	}
	
	@Override
	public boolean deleteAll() {
		boolean status = true;
		
		File root = new File(localRoot);
		File [] files = root.listFiles();		
		for(File file : files){
			if(file.isDirectory()){
				boolean success = deleteFolder(file.getAbsolutePath(), true);
				if(!success){
					status = false;
					log.info("failed deleting:" + file.getAbsolutePath());
				}
			}else{
				status = file.delete();
				if(!status){
					log.info("failed deleting:" + file.getAbsolutePath());
				}
			}
		}
		
		return status;
	}

	@Override
	public InputStream download(String targetFileName) {
		FileInputStream fis = null;
		
		try{
			Path path = Paths.get(targetFileName);
			log.info("Opened FileInputStream:" + path.toFile().getAbsolutePath());
			fis = new FileInputStream(path.toFile());					
		}catch(Exception e){
			log.debug("Error on downloading", e);
		}
		
		return fis;		
	}

	@Override
	public boolean deleteFolder(String folder, boolean recursively) {
		boolean status = true;
		if(recursively){			
			try{
				FileUtils.forceDelete(new File(folder));
			}catch(Exception e){
				log.debug("ERROR:", e);
				status = false;
			}			
			return status;
		}else{
			status = Paths.get(folder).toFile().delete();			
		}
		
		return status;
	}

	@Override
	public boolean compressDir(String dir, String zipFile) {		
		try{			
			DirCompressor compressor = new DirCompressor(localRoot);
			return compressor.zipDir(zipFile, dir);			
		}catch(Exception e){
			log.debug("ERROR", e);
			return false;	
		}
		
	}
	
	
}
