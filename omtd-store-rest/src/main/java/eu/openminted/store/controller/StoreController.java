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
    @RequestMapping(value="/store/createArchive", method=RequestMethod.GET)
    @ResponseBody
    public String createArchive(){
    	return storeService.createArchive() + "";    
    }

    /**
     * Creates a subArchive under a given archive.
     * @return
     */
    @RequestMapping(value="/store/createSubArchive", method=RequestMethod.POST)
    @ResponseBody
    public String createSubArchive(@RequestParam("archiveID") String archiveId, @RequestParam("archiveName") String archiveName){
    	return storeService.createArchive(archiveId, archiveName) + "";    
    }

    /**
     * Finalize archive
     * @return
     */
    @RequestMapping(value="/store/finalizeArchive", method=RequestMethod.POST)
    @ResponseBody
    public String finalizeArchive(@RequestParam("archiveID") String archiveId){
    	return storeService.finalizeArchive(archiveId) + "";
    }
   
    	
    /**
     * Download archive
     * @return
     */
    @RequestMapping(value="/store/downloadArchive", method=RequestMethod.POST)
    @ResponseBody
    public  ResponseEntity<Resource> downloadArchive(@RequestParam("archiveID") String archiveId){
        InputStream fileInputStream = storeService.downloadArchive(archiveId);
        return Utils.download(fileInputStream, archiveId);
    }
    
    /**
     * List files
     * @return a list of files.
     */
    @RequestMapping(value="/store/listFiles", method=RequestMethod.GET)
    @ResponseBody
    public String listFiles(){
    	return storeService.listAllFiles();     
    }
    
    /**
     * Delete all files.
     * @return action status
     */
    @RequestMapping(value="/store/deleteAll", method=RequestMethod.GET)
    @ResponseBody
    public String deleteAll(){
    	return storeService.deleteAll() + "";    
    }
    
    /**
     * Delete archive.
     * @return action status
     */
    @RequestMapping(value="/store/deleteArchive", method=RequestMethod.POST)
    @ResponseBody
    public String deleteArhive(@RequestParam("archiveID") String archiveId){
    	return "" + storeService.deleteArchive(archiveId, true);    
    }    
    
    /**
     * Upload file.
     * @param archiveId
     * @param fileName
     * @param file
     * @return action status.
     */
    @RequestMapping(value="/store/uploadFile", method=RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam("archiveID") String archiveId, @RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) {
    	String result = "";
    	try{
    		result = "" + storeService.storeFile(archiveId, file.getInputStream(), fileName);
    	}catch(Exception e){
    		log.debug("ERROR", e);
    		result = "false";
    	}
    	
    	return result;
    }
    
    /** 
     * Download file
     * @param fname
     * @return
     */
    @RequestMapping(value="/store/downloadFile", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fname){
       	    	
        InputStream fileInputStream = storeService.downloadFile(fname);
        return Utils.download(fileInputStream, fname);
	}
    
}
