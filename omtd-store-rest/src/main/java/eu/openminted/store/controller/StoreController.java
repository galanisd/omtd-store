package eu.openminted.store.controller;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eu.openminted.store.StoreException;
import eu.openminted.store.StoreService;
import eu.openminted.store.common.StoreREST;
import eu.openminted.store.common.StoreResponse;

/**
 * A Spring Controller for the Store Service.
 * @author galanisd
 *
 */
@Controller
public class StoreController {

	private static final Logger log = LoggerFactory.getLogger(StoreController.class);
	
    private final StoreService storeService;

    /**
     * Constructor.
     * @param storeService
     */
    @Autowired
    public StoreController(StoreService storeService) {
       this.storeService = storeService;
    }   
    
    /**
     * Creates an archive.
     * @return
     */
    @RequestMapping(value=StoreREST.createArchive, method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public StoreResponse createArchive(){
    	String response = String.valueOf(storeService.createArchive()); 
    	return new StoreResponse(response, "");    
    }

    /**
     * Creates a subArchive under a given archive.
     * @return
     */
    @RequestMapping(value=StoreREST.createSubArchive, method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public StoreResponse createSubArchive(@RequestParam(StoreREST.archiveID) String archiveId, @RequestParam("archiveName") String archiveName){
    	String response = storeService.createArchive(archiveId, archiveName);
    	return new StoreResponse(response, "");
    }

    /**
     * Finalize archive
     * @return
     */
    @RequestMapping(value=StoreREST.finalizeArchive, method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public StoreResponse finalizeArchive(@RequestParam(StoreREST.archiveID) String archiveId){
    	String response = String.valueOf(storeService.finalizeArchive(archiveId));
    	return new StoreResponse(response, "");
    }
   
    	
    /**
     * Download archive
     * @return
     */
    @RequestMapping(value=StoreREST.downloadArchive, method=RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<Resource> downloadArchive(@RequestParam(StoreREST.archiveID) String archiveId){
        InputStream fileInputStream = storeService.downloadArchive(archiveId);
        return Utils.download(fileInputStream, archiveId);
    }
    
    /**
     * List files
     * @return a list of files.
     */
    @RequestMapping(value=StoreREST.listFiles, method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public StoreResponse listFiles(){
    	String response = storeService.listAllFiles();  
    	return new StoreResponse(response, "");    	   
    }
    
    /**
     * Delete all files.
     * @return action status
     */
    @RequestMapping(value=StoreREST.deleteAll, method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public StoreResponse deleteAll(){
    	String response = String.valueOf(storeService.deleteAll());
    	return new StoreResponse(response, "");  
    }
    
    /**
     * Delete archive.
     * @return action status
     */
    @RequestMapping(value=StoreREST.deleteArchive, method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public StoreResponse deleteArhive(@RequestParam(StoreREST.archiveID) String archiveId){
    	String response = String.valueOf(storeService.deleteArchive(archiveId, true));
    	return new StoreResponse(response, "");
    }    
    
    /**
     * Upload file.
     * @param archiveId
     * @param fileName
     * @param file
     * @return action status.
     */
    @RequestMapping(value=StoreREST.uploadFile, method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public StoreResponse uploadFile(@RequestParam(StoreREST.archiveID) String archiveId, @RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) {
    	StoreResponse resp = new StoreResponse();
    	String response = ""; 
    	
    	try{
    		response = String.valueOf(storeService.storeFile(archiveId, file.getInputStream(), fileName));
    		resp.setResponse(response);
    	}catch(Exception e){
    		log.debug("ERROR", e);
    		resp.setResponse(String.valueOf(Boolean.FALSE));
    		resp.setReport(e.getMessage());
    	}
    	
    	return resp;
    }
    
    /** 
     * Download file
     * @param fname
     * @return
     */
    @RequestMapping(value=StoreREST.downloadFile, method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fname){
       	    	
        InputStream fileInputStream = storeService.downloadFile(fname);
        return Utils.download(fileInputStream, fname);
	}
    
}
