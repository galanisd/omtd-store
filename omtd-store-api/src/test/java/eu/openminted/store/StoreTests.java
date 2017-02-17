package eu.openminted.store;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.config.ApplicationConfigurator;
import eu.openminted.store.test.StoreAPITester;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ApplicationConfig.class/*, loader = AnnotationConfigContextLoader.class*/)
@ContextConfiguration(initializers = TestApplicationContextInitializer.class, classes = ApplicationConfig.class/*, loader = AnnotationConfigContextLoader.class*/)
public class StoreTests {

	private static final Logger log = LoggerFactory.getLogger(StoreTests.class);

	private StoreAPITester appTester;
	private File sampleAnnotatedFile;
	private File samplePDFFile;		
	
	@Autowired
	private StoreServiceGeneric store;
	
	@BeforeClass
	public static void beforeClass() {
		//ApplicationConfigurator appConfigtr = new ApplicationConfigurator();
		//appConfigtr.configure();
	}
	
	@Before
	public void beforeEachTest() {		
		log.info("Load everything I need before running test.");		
		appTester = new StoreAPITester(store);		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		sampleAnnotatedFile = new File(classLoader.getResource(StoreAPITester.gateDoc).getFile());
		samplePDFFile = new File(classLoader.getResource(StoreAPITester.pdfDoc).getFile());				
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
	public void afterEachTest(){
		try{
			log.info("Clean up.");
			appTester.listAllFilesAndThenDeleteAll();
		}catch(Exception e){
			log.info("ERROR:", e);
		}
	}
}
