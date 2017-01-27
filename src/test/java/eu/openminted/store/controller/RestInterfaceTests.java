package eu.openminted.store.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;

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

import eu.openminted.store.StoreService;
import eu.openminted.store.config.ApplicationConfig;
import eu.openminted.store.config.ApplicationConfigurator;
import eu.openminted.store.restclient.StoreRESTClient;
import eu.openminted.store.test.Store_API_Tester;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestInterfaceTests {

	private static final Logger log = LoggerFactory.getLogger(RestInterfaceTests.class);
	
    //@MockBean
    //ApplicationConfig config;

    //@MockBean
    StoreRESTClient store;
    
    @LocalServerPort
    private int randomServerPort;

    // === == === == === ==    
	@BeforeClass
	public static void beforeClass() {
		ApplicationConfigurator.configure();		
	}
	
	@Before
	public void beforeTests() {
		store = new StoreRESTClient();
		log.info("endpoint:" + store.getEndpoint() + " " + randomServerPort);
		store.setEndpoint("http://localhost:" + randomServerPort);
		log.info("endpoint was set to:" + store.getEndpoint());
	}
	
    @Test
    public void test01deleteAll() throws Exception {
    	assertTrue (store.deleteAll().equalsIgnoreCase("true"));    	
    }

    @Test
    public void test02CreateArch() throws Exception {
    	assertNotNull(store.createArchive());    	
    }
    
        
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

