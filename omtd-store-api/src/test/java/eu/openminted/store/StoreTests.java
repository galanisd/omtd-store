package eu.openminted.store;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.config.ApplicationConfigurator;
import eu.openminted.store.test.Store_API_Tester;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigContextLoader.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StoreTests {

	private static final Logger log = LoggerFactory.getLogger(StoreTests.class);

	private Store_API_Tester appTester;
	private File sampleAnnotatedFile;
	private File samplePDFFile;
	
	@Autowired
	private StoreServiceGeneric store;
	
	@BeforeClass
	public static void beforeClass() {
		ApplicationConfigurator.configure();
	}
	
	@Before
	public void beforeTests() {
		log.info("Load everything I need before running tests.");		
		appTester = new Store_API_Tester(store);		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		sampleAnnotatedFile = new File(classLoader.getResource(Store_API_Tester.gateDoc).getFile());
		samplePDFFile = new File(classLoader.getResource(Store_API_Tester.pdfDoc).getFile());
	}
	
	@Test
	public void clearStore() throws Exception{
		appTester.listAllFilesAndThenDeleteAll();		
		String fileList = store.listAllFiles();		
		assertTrue(fileList.isEmpty());		
	}

	@Test
	public void createAHierarchyOfArchivesAndStoreAFileInTheLastOne() throws Exception{
		boolean status = appTester.createAHierarchyOfArchivesAndStoreAFileInTheLastOne(sampleAnnotatedFile);		
		assertTrue(status);		
	}
	
	@Test
	public void createArchiveWithAFolderThatContainsAPDFFile() throws Exception{
		boolean status = appTester.createArchiveWithAFolderThatContainsAPDFFile(samplePDFFile);		
		assertTrue(status);		
	}	
	
	@Test
	public void createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile() throws Exception{
		boolean status = appTester.createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(sampleAnnotatedFile);		
		assertTrue(status);		
	}
	
	@After 
	public void tearDown(){
		try{			
			appTester.listAllFilesAndThenDeleteAll();
		}catch(Exception e){
			log.info("ERROR:", e);
		}
	}
}
