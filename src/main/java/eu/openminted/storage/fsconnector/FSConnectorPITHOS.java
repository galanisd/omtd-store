package eu.openminted.storage.fsconnector;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

//import eu.openminted.storage.fsconnector.debug.HadoopPithosConnector;
import gr.grnet.escience.pithos.rest.HadoopPithosConnector;
import gr.grnet.escience.fs.pithos.PithosFileSystem;
import gr.grnet.escience.fs.pithos.PithosObject;
import gr.grnet.escience.fs.pithos.PithosOutputStream;
import gr.grnet.escience.fs.pithos.PithosPath;

/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorPITHOS implements FSConnector {

	private  HadoopPithosConnector connector;	
	
	public String defaultContainer = "OMTD";
	private String pithosUrl;
	
	/**
	 * Constructor.
	 * 
	 * @param pithosUrl
	 * @param pithosToken
	 * @param uuid
	 */
	public FSConnectorPITHOS(String pithosUrl, String pithosToken, String uuid) {
		try {
			this.pithosUrl = pithosUrl;
			connector = new HadoopPithosConnector(pithosUrl, pithosToken, uuid);
			// workaround.
			PithosFileSystem.setHadoopPithosConnector(connector);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean makeFolder(String fileName) {
		System.out.println("uploading:" + fileName);
		String rel = fileName.substring(pithosUrl.length());
		System.out.println("rel:" + rel);

		String resultCode = "-1";
		resultCode = connector.uploadFileToPithos(defaultContainer, fileName.substring(pithosUrl.length()), true);

		System.out.println("resultCode:" + resultCode);
		if (resultCode != null && resultCode.equals("201")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {		

		try {
			connector.storePithosObject(defaultContainer, new PithosObject(targetFileName, null));
			org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
			
			String target = targetFileName.substring(pithosUrl.length());
			PithosOutputStream pithosOutputStream = new PithosOutputStream(conf,
					new PithosPath(defaultContainer, target), connector.getPithosBlockDefaultSize(defaultContainer), 1 * 1024 * 1024);

			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 10];

			while ((read = is.read(bytes)) != -1) {
				pithosOutputStream.write(bytes, 0, read);
			}
			
			is.close();
			//pithosOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public String listAllFiles() {				
		String result = connector.getFileList(defaultContainer);	
		
        String[] filePaths = result.split("\\s+");
        for (String file : filePaths) {
        	result = result + file + "\n";        		        		
        }
        
		return result;
	}
	
	@Override
	public boolean deleteFile(String fileName) {
		System.out.println("deleting:" + fileName);
		String resultCode = connector.deletePithosObject(defaultContainer, fileName);		
		System.out.println("resultCode:" + resultCode);
		
		if (resultCode.contains("204")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAll() {
		String result = connector.getFileList(defaultContainer);				
		
        String[] filePaths = result.split("\\s+");
        for (String file : filePaths) {        	
        	result = result + file + "\n";
    		System.out.println("DELETE:" + file + " " + deleteFile(file));        		        	
        }
       
        return true;
	}

}
