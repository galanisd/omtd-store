package eu.openminted.store.test;

import java.io.File;

//import org.junit.rules.TestRule;
//import org.junit.runner.Description;
//import org.junit.runners.model.Statement;

/**
 * @author galanisd
 *
 */
public class TestFiles{

	public static final String gateDoc = "eu/openminted/store/rizospastis_AVVd-4ehg3d853ySFFif.xml.gate";
	public static final String pdfDoc = "eu/openminted/store/2016_Kathaa_NAACL.pdf";
	
	File sampleAnnotatedFile;
	File samplePDFFile;
	
	public TestFiles(){
		load();
	}
	
	private void load(){
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		sampleAnnotatedFile = new File(classLoader.getResource(TestFiles.gateDoc).getFile());
		samplePDFFile = new File(classLoader.getResource(TestFiles.pdfDoc).getFile());	
	}

	public File getSampleAnnotatedFile() {
		return sampleAnnotatedFile;
	}

	public void setSampleAnnotatedFile(File sampleAnnotatedFile) {
		this.sampleAnnotatedFile = sampleAnnotatedFile;
	}

	public File getSamplePDFFile() {
		return samplePDFFile;
	}

	public void setSamplePDFFile(File samplePDFFile) {
		this.samplePDFFile = samplePDFFile;
	}

}
