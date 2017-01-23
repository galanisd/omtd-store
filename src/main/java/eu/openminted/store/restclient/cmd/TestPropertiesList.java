package eu.openminted.store.restclient.cmd;

import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author galanisd
 *
 */
public class TestPropertiesList {

	private final static Logger log = LoggerFactory.getLogger(TestPropertiesList.class);
	private HashMap<String, Properties> map;

	public TestPropertiesList() {
		map = new HashMap<String, Properties>();
	}
	
	public void loadTestFileProps(String[] names){
		for(int i = 0; i < names.length && names != null; i++){
			Properties props = loadProps(names[i]);
			map.put(names[i], props);
		}
	}
	
	private Properties loadProps(String name){
		Properties props = new Properties();		
		String cmdProps = System.getProperty(name);
		
		try
		{
			if(cmdProps != null){
				//props.load();
			}else{
				props.load(TestPropertiesList.class.getResourceAsStream(getPropertiesFileName(name)));
			}
		}catch(Exception e){
			log.info("ERROR ON lOADING:", e);
		}
		
		return props;
	}
	
	private String getPropertiesFileName(String name){
		return name + "Client.properties";
	}

	public HashMap<String, Properties> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Properties> map) {
		this.map = map;
	}
	
	
}
