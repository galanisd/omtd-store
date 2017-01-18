package eu.openminted.store.fsconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DirCompressor {

	private String rootStore;
	
	public DirCompressor(String rootStore){
		this.rootStore = rootStore;		
	}
	
	public void zipDir(String zipFileName, String dir) throws Exception {
		File dirObj = new File(dir);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		System.out.println("Creating : " + zipFileName);
		addDir(dirObj, out);
		out.close();
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
			System.out.println(" Adding: " + files[i].getAbsolutePath());
			
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
