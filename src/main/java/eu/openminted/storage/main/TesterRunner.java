package eu.openminted.storage.main;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import eu.openminted.storage.StoreService;
import eu.openminted.storage.StoreServiceLocalDisk;
import eu.openminted.storage.StoreServicePITHOS;
import eu.openminted.storage.config.ConfigLocal;
import eu.openminted.storage.config.ConfigPITHOS;

/**
 * @author galanisd
 *
 */
public class TesterRunner {

	public static void main(String args[]){
		try{
			 int t = 2;
			 
			 ApplicationContext ctx;
			 StoreService store;
			 
			 if( t == 1){
				 ctx = new AnnotationConfigApplicationContext(ConfigLocal.class);				   
				 store = (StoreServiceLocalDisk)ctx.getBean(StoreServiceLocalDisk.class);
			 }else{
				 ctx = new AnnotationConfigApplicationContext(ConfigPITHOS.class);
				 store = (StoreServicePITHOS)ctx.getBean(StoreServicePITHOS.class);	 
			 }
			 			 			 
			 ClassLoader classLoader = ClassLoader.getSystemClassLoader();			 			 			 
			 File sampleAnnotatedFile = new File(classLoader.getResource("eu/openminted/storage/rizospastis_AVVd-4ehg3d853ySFFif.xml.gate").getFile());
			 File samplePDFFile = new File(classLoader.getResource("eu/openminted/storage/2016_Kathaa_NAACL.pdf").getFile());
			 
			 long start = System.currentTimeMillis();
			 
			 // -- Scenario 0
			 store.deleteAll();
			 
			 // -- Scenario 1
			 // Creating a hierarchy of archives. 
			 String level0ArchId = store.createArchive();
			 System.out.println("Created archive level 0:" + level0ArchId);				 
			 String level1ArchId = store.createArchive(level0ArchId, "");			 
			 System.out.println("Created archive level 1:" + level1ArchId);				 
			 String level2ArchId = store.createArchive(level1ArchId, "");
			 System.out.println("Created archive level 2:" + level2ArchId);			 
			 // Store a file in the last archive
			 store.storeFile(level2ArchId, new FileInputStream(sampleAnnotatedFile), sampleAnnotatedFile.getName());			 

			 // -- Scenario 2
			 // Creating an archive. 
			 String archId = store.createArchive();
			 // Store a PDF
			 store.storeFile(archId, new FileInputStream(samplePDFFile), "pdfs/" + samplePDFFile.getName());

			 // -- Scenario 3
			 // Creating an archive. 
			 archId = store.createArchive();
			 // Store an annotation file.
			 store.storeFile(archId, new FileInputStream(sampleAnnotatedFile), "annotations/" + sampleAnnotatedFile.getName());
			 
			 // -- Scenario 4
			 // Creating an archive. 
			 archId = store.createArchive();
			 // Store an annotation file.
			 store.storeFile(archId, new FileInputStream(sampleAnnotatedFile), "annotations/" + sampleAnnotatedFile.getName());
			 store.deleteFile(archId, "annotations/" + sampleAnnotatedFile.getName());
			 		
			 // -- Scenario 5
			 // Creating an archive. 
			 archId = store.createArchive();
			 //File inputDir = new File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned_toy/");
			 File inputDir = new File("C:/Users/galanisd/Desktop/Data/CORE/repository_metadata_2015-09_abstractscleaned/");
			 File[] inFiles = inputDir.listFiles();
			 
			 int counter = 0;
			 int threshold = 1000;
			 for(File fileForUpload : inFiles){
				if(counter < threshold){
					//boolean success = store.storeFile(archId, new FileInputStream(fileForUpload), "abstracts/" + fileForUpload.getName());
					//System.out.println(counter + " uploaded:" + success + " " + fileForUpload.getAbsolutePath());
					
				}
				counter++;
			 }
			 
			 System.out.println("\n\n\n" + "FILE LIST");
			 System.out.println(store.listAllFiles());
			 long end = System.currentTimeMillis();
			 
			 System.err.println("Duration:" + (end-start)/60000);
		}catch(Exception e){
			e.printStackTrace();
		}		 		 
	}
}
