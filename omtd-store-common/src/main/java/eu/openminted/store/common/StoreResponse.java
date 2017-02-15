package eu.openminted.store.common;

/**
 * @author galanisd
 *
 */
public class StoreResponse {
	
	private String response;
	private String report;
	
	public StoreResponse(){
		response = "";
		report = "";
	}
	
	public StoreResponse(String response, String report) {
		super();
		this.response = response;
		this.report = report;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}

	@Override
	public String toString() {
		return "StoreResponse [response=" + response + ", report=" + report + "]";
	}
}
