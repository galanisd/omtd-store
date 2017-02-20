package eu.openminted.store.fsconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.openminted.store.core.StoreServiceGeneric;

public class DirCompressor {

	private static final Logger log = LoggerFactory.getLogger(DirCompressor.class);
	
	private String rootStore;
	
	public DirCompressor(String rootStore){
		this.rootStore = rootStore;		
	}
	
	/**
	 * Zips directory.
	 * @param zipFileName
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public boolean zipDir(String zipFileName, String dir) {
		File dirObj = new File(dir);
		
		if(dirObj.exists()){
			try{
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
				log.info("Creating : " + zipFileName);
				addDir(dirObj, out);
				out.close();
			} catch (Exception e){
				log.debug("ERROR" , e);
				return false;
			}
		}else{
			return false;
		}
		
		return true;
	}

	private void addDir(File dirObj, ZipOutputStream out) throws IOException {
		File[] files = dirObj.listFiles();
		byte[] tmpBuf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDir(files[i], out);
				continue;
			}
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
			log.debug(" Adding: " + files[i].getAbsolutePath());
			
			String rel = files[i].getAbsolutePath().substring(rootStore.length() + 1);
			
			out.putNextEntry(new ZipEntry(rel));
			int len;
			while ((len = in.read(tmpBuf)) > 0) {
				out.write(tmpBuf, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}

	public static void main(String[] args) throws Exception {
	    //zipDir(args[0], args[1]);
		
		String dir = "C:/Users/galanisd/Desktop/Data/_AppTestData/StorageRoot/";
		DirCompressor compressor = new DirCompressor(dir);
		
		File d = new File(dir);
		File[] files = d.listFiles();
		
		for(File file : files){
			if(file.isDirectory()){
				compressor.zipDir(file.getParent() + "/" + file.getName() + ".zip" , file.getAbsolutePath());
			}
		}
	  }
}
