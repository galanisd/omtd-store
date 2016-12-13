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

import eu.openminted.store.StoreException;
import eu.openminted.store.StoreService;

/**
 * A Spring Controller for the Store Service.
 * @author galanisd
 *
 */
@Controller
public class StoreController {

	private static final Logger log = LoggerFactory.getLogger(StoreController.class);
	
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
       this.storeService = storeService;
    }   
    
    @RequestMapping(value="/store/createArchive", method=RequestMethod.GET)
    @ResponseBody
    public String createArchive(){
    	return storeService.createArchive() + "";    
    }
    
    @RequestMapping(value="/store/listFiles", method=RequestMethod.GET)
    @ResponseBody
    public String listFiles(){
    	return storeService.listAllFiles();     
    }
    
    @RequestMapping(value="/store/deleteAll", method=RequestMethod.GET)
    @ResponseBody
    public String deleteAll(){
    	return storeService.deleteAll() + "";    
    }
    
    @RequestMapping(value="/store/downloadFile", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName){
       	    	
        InputStream is = storeService.downloadFile(fileName);
        Resource resource  = null;
        
        try {            
            resource = new InputStreamResource(is);
            if(!resource.exists() || !resource.isReadable()) {
            	throw new StoreException("Could not read file: " + fileName);
            }
            
        } catch (StoreException e) {
            throw new StoreException("Could not read file: " + fileName, e);
        }
            
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);    
            	
    }
}
