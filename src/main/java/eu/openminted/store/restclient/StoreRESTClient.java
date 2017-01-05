package eu.openminted.store.restclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

/**
 * @author galanisd
 *
 */
public class StoreRESTClient {

	private static final Logger log = LoggerFactory.getLogger(ApplicationBoot.class);

	private RestTemplate restTemplate;
	private String endpoint;

	/**
	 * Constructor.
	 * 
	 * @param endpoint
	 */
	public StoreRESTClient(String endpoint) {
		this.restTemplate = new RestTemplate();
		this.endpoint = endpoint;
	}

	public String listFiles() {
		String response = restTemplate.getForObject(endpoint + "/store/listFiles/", String.class);
		return response;
	}

	public String createArchive() {
		String response = restTemplate.getForObject(endpoint + "/store/createArchive/", String.class);
		return response;
	}

	public String deleteAll() {
		String response = restTemplate.getForObject(endpoint + "/store/deleteAll/", String.class);
		return response;
	}
	
	public String updload(File file, String archiveID, String fileName) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("file", new FileSystemResource(file));
		map.add("fileName", fileName);
		map.add("archiveID", archiveID);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		restTemplate.setRequestFactory(requestFactory);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);	
		restTemplate.postForEntity(endpoint + "/store/uploadFile/", requestEntity, String.class);
		
		return "";
	}
	
	public boolean download(String fileName, String target) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("fileName", fileName);
		
		FileOutputStream output = null;
		try{
			output = new FileOutputStream(target);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			
			final ResponseExtractor<ResponseEntity<File>> responseExtractor =
			        new ResponseExtractor<ResponseEntity<File>>() {

			    // (2)
			    @Override
			    public ResponseEntity<File> extractData(ClientHttpResponse response)
			            throws IOException {

			        File rcvFile = File.createTempFile("rcvFile", "zip");

			        FileCopyUtils.copy(response.getBody(), new FileOutputStream(rcvFile));

			        return ResponseEntity.status(response.getStatusCode())
			                .headers(response.getHeaders()).body(rcvFile);
			    }

			};

			File getFile = null;
			//ResponseEntity<File> entity = restTemplate.execute(endpoint + "/store/downloadFile/", map, InputStream.class);
			ResponseEntity<File> responseEntity  = restTemplate.execute(endpoint + "/store/downloadFile/", HttpMethod.POST, null, responseExtractor, map);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			    getFile = responseEntity.getBody();
			}
			FileInputStream fis = new FileInputStream(getFile);
			
			try{
				int read = 0;
				byte[] bytes = new byte[1024 * 1024 * 10];
				while ((read = fis.read(bytes)) != -1) {
					output.write(bytes, 0, read);
					output.flush();
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			log.debug("ERROR", e);
			return false;
		}
		
		return true;
		
	}
	
	
}
