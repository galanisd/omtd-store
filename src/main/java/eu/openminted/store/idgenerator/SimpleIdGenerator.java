package eu.openminted.store.idgenerator;

import java.util.UUID;

public class SimpleIdGenerator implements IdGenerator {

	@Override
	public String generateId() {
		return UUID.randomUUID().toString();		
	}


}
