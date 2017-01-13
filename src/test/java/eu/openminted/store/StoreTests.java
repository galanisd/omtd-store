package eu.openminted.store;

import static org.junit.Assert.*;

import java.io.File;

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
import eu.openminted.store.main.AppTester;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StoreTests {

	private static final Logger log = LoggerFactory.getLogger(StoreTests.class);

	private AppTester tester;
	private File sampleAnnotatedFile;
	File samplePDFFile;
	
	@Autowired
	private StoreServiceGeneric store;
	
	@BeforeClass
	public static void beforeClass() {
		ApplicationConfigurator.configure();
	}
	
	@Before
	public void beforeTests() {
		log.info("Load everyhting I need before running tests.");		
		tester = new AppTester(store);		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		sampleAnnotatedFile = new File(classLoader.getResource(AppTester.gateDoc).getFile());
		samplePDFFile = new File(classLoader.getResource(AppTester.pdfDoc).getFile());
	}
	
	@Test
	public void test00() throws Exception{
		tester.listAllFilesAndThenDeleteAll();		
		String fileList = store.listAllFiles();		
		assertTrue(fileList.isEmpty());		
	}

	@Test
	public void test01() throws Exception{
		boolean status = tester.createAHierarchyOfArchivesAndStoreAFileInTheLastOne(sampleAnnotatedFile);		
		assertTrue(status);		
	}
	
	@Test
	public void test02() throws Exception{
		boolean status = tester.createArchiveWithAFolderThatContainsAPDFFile(samplePDFFile);		
		assertTrue(status);		
	}	
	
	@Test
	public void test03() throws Exception{
		boolean status = tester.createArchiveWithAFolderThatContainsAnAnnotationFileThenDeleteTheAnnotationFile(sampleAnnotatedFile);		
		assertTrue(status);		
	}
}
