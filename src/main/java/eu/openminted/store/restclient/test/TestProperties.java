package eu.openminted.store.restclient.test;

import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author galanisd
 *
 */
public class TestProperties {

	private final static Logger log = LoggerFactory.getLogger(TestProperties.class);	
	public final static String testPropertiesCfg = "testPropertiesCfg"; 
	
	public static Properties loadProps(){
		Properties props = new Properties();		
		String cmdProps = System.getProperty(testPropertiesCfg);
		
		try
		{
			if(cmdProps != null){
				//props.load();
			}else{
				props.load(TestProperties.class.getResourceAsStream(testPropertiesCfg + ".properties"));
			}
		}catch(Exception e){
			log.info("ERR0R ON lOADING:", e);
		}
		
		return props;
	}
	
}
