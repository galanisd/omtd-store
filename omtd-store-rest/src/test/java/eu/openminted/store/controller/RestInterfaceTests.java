package eu.openminted.store.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;

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
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.config.ApplicationConfigurator;
import eu.openminted.store.core.StoreService;
import eu.openminted.store.restclient.StoreRESTClient;
import eu.openminted.store.test.StoreAPITester;



@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = {ApplicationBoot.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestInterfaceTests {

	private static final Logger log = LoggerFactory.getLogger(RestInterfaceTests.class);
	
    private StoreRESTClient store;
    
    @LocalServerPort
    private int randomServerPort;

    // === == === == === ==    
	@BeforeClass
	public static void beforeClass() {
		ApplicationConfigurator appConfigtr = new ApplicationConfigurator();
		appConfigtr.configure();	
	}
	
	/**
	 * Runs before each test. 
	 */
	@Before
	public void beforeEachTest() {				
		// Create Store Client.
		store = new StoreRESTClient();
		store.setEndpoint("http://localhost:" + randomServerPort);		
		log.info("endpoint was set to:" + store.getEndpoint());		
	}
	
	// All tests are below.
	// === == === == === ==    
    /**
     * Delete all files in the store.
     * @throws Exception
     */
    @Test
    public void deleteAll() throws Exception {
    	assertTrue (store.deleteAll().getResponse().equalsIgnoreCase("true"));    	
    }

    /**
     * Creates an archive.
     * @throws Exception
     */
    @Test
    public void createArch() throws Exception {
    	String archId = store.createArchive().getResponse();
    	assertNotNull(archId);    	
    }

    /**
     * Creates and deletes an archive.
     * @throws Exception
     */
    @Test
    public void deleteArch() throws Exception {
    	String archId = store.createArchive().getResponse();
    	assertTrue(archId != null && store.deleteArchive(archId).getResponse().equalsIgnoreCase("true"));
    }

    /**
     * Creates a subarchive.
     * @throws Exception
     */
    @Test
    public void createSubArchive() throws Exception {
    	String archId = store.createArchive().getResponse();
    	String subArchId = store.createSubArchive(archId, "anArchive").getResponse();
    	assertTrue(archId != null && subArchId != null);
    }
    
    /**
     * Uploads a file in an archive.
     * @throws Exception
     */
    @Test
    public void uploadFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("test.txt", getClass());
        String archiveID = store.createArchive().getResponse();        
        String status = store.storeFile(resource.getFile(), archiveID, resource.getFile().getName()).getResponse();
        assertTrue(archiveID != null && status.equalsIgnoreCase("true"));
    }
    // === == === == === ==  
    
    /**
     * Tear down/clean up after test. 
     * @throws Exception
     */
    @After
    public void afterEachTest() throws Exception {
    	log.info("Clean Up.");
    	store.deleteAll();    	
    }
    
    // == === ==
    
    /*
    @Test
    public void shouldUploadFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("file", resource);
        ResponseEntity<String> response = this.restTemplate.postForEntity("/", map, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).startsWith("http://localhost:" + this.port + "/");
        //then(storeService).should().s(any(MultipartFile.class));
    }
    
    @Test
    public void shouldDownloadFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("testupload.txt", getClass());
        given(this.storageService.loadAsResource("testupload.txt")).willReturn(resource);

        ResponseEntity<String> response = this.restTemplate
                .getForEntity("/files/{filename}", String.class, "testupload.txt");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename=\"testupload.txt\"");
        assertThat(response.getBody()).isEqualTo("Spring Framework");
    }
	*/
    

}

