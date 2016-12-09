package eu.openminted.store.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.openminted.store.StoreService;

@Controller
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
       this.storeService = storeService;
    }    
    
    @RequestMapping(value="/store/listfiles", method=RequestMethod.GET)
    @ResponseBody
    public String listFiles(){    	       	    	
    	return storeService.listAllFiles();     
    }
    
    @RequestMapping(value="/store/deleteall", method=RequestMethod.GET)
    @ResponseBody
    public String deleteAll(){    	       	    	
    	return storeService.deleteAll() + "";    
    }
    
    /*
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String listUploadedFiles(Model model) throws IOException {        
        return "uploadForm";
    }
    */

}
