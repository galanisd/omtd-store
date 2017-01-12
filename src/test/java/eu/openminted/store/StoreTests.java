package eu.openminted.store;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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

	@Autowired
	private StoreServiceGeneric store;

	private AppTester tester;
	private File sampleAnnotatedFile;
	
	@BeforeClass
	public static void beforeClass() {
		ApplicationConfigurator.configure();
	}
	
	@Before
	public void beforeTests() {
		System.out.println("@Before");		
		tester = new AppTester(store);
		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		sampleAnnotatedFile = new File(classLoader.getResource(AppTester.gateDoc).getFile());
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
	
}
