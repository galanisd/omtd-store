package eu.openminted.storage.fsconnector;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import eu.openminted.storage.fsconnector.debug.HadoopPithosConnector;
import gr.grnet.escience.fs.pithos.PithosObject;
import gr.grnet.escience.fs.pithos.PithosOutputStream;
import gr.grnet.escience.fs.pithos.PithosPath;

/**
 * 
 * @author galanisd
 *
 */
public class FSConnectorPITHOS implements FSConnector {

	private HadoopPithosConnector connector;
	public String defaultContainer = "athena";

	/**
	 * Constructor.
	 * 
	 * @param pithosUrl
	 * @param pithosToken
	 * @param uuid
	 */
	public FSConnectorPITHOS(String pithosUrl, String pithosToken, String uuid) {
		try {
			connector = new HadoopPithosConnector(pithosUrl, pithosToken, uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean makeFolder(String fileName) {
		System.out.println("uploading:" + fileName);
		String rel = fileName.substring("pithos://athena/".length());
		System.out.println("rel:" + rel);

		String resultCode = "-1";
		resultCode = connector.uploadFileToPithos(defaultContainer, fileName.substring("pithos://athena/".length()),
				true);
		// System.out.println(connector.getFileList("athena").indexOf(fileName));

		System.out.println("resultCode:" + resultCode);
		if (resultCode != null && resultCode.equals("201")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean storeFile(String targetFileName, InputStream is) {
		connector.storePithosObject(defaultContainer, new PithosObject(targetFileName, null));

		try {
			org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
			
			String target = targetFileName.substring("pithos://athena/".length());
			PithosOutputStream pithosOutputStream = new PithosOutputStream(conf,
					new PithosPath(defaultContainer, target), connector.getPithosBlockDefaultSize(defaultContainer), 1 * 1024 * 1024);

			IOUtils.copy(is, pithosOutputStream);			
			pithosOutputStream.close();
			is.close();

		} catch (Exception e) {
			return false;
		}

		return true;
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
	public String listAllFiles() {
		System.out.println("\n\n\n" + "FILE LIST");		
		String result = connector.getFileList("athena");
		
		String filteredResult = "";
		
        String[] filePaths = result.split("\\s+");
        for (String file : filePaths) {
        	if(!file.startsWith("clarinel")){
        		filteredResult = filteredResult + file + "\n";        		        		
        	}
        }
        
		return filteredResult;
	}

	@Override
	public boolean deleteAll() {
		String result = connector.getFileList("athena");		
		String filteredResult = "";
		
        String[] filePaths = result.split("\\s+");
        for (String file : filePaths) {
        	if(!file.startsWith("clarinel")){
        		filteredResult = filteredResult + file + "\n";
        		System.out.println("DELETE:" + file + " " + deleteFile(file));        		
        	}
        }
       
        return true;
	}

}
