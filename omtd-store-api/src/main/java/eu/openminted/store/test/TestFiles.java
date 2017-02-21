package eu.openminted.store.test;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

//import org.junit.rules.TestRule;
//import org.junit.runner.Description;
//import org.junit.runners.model.Statement;

/**
 * @author galanisd
 *
 */
public class TestFiles {

	private static final Logger log = LoggerFactory.getLogger(TestFiles.class);

	public static final String gateDoc = "/eu/openminted/store/test/rizospastis_AVVd-4ehg3d853ySFFif.xml.gate";
	public static final String pdfDoc = "/eu/openminted/store/test/2016_Kathaa_NAACL.pdf";

	File sampleAnnotatedFile;
	File samplePDFFile;

	public TestFiles() {
		load();
	}

	private void load() {
		try {
			// ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			// sampleAnnotatedFile = new
			// File(classLoader.getResource(TestFiles.gateDoc).getFile());
			// samplePDFFile = new
			// File(classLoader.getResource(TestFiles.pdfDoc).getFile());

			ClassPathResource resource1 = new ClassPathResource(gateDoc, getClass());
			sampleAnnotatedFile = getFile(resource1);
			ClassPathResource resource2 = new ClassPathResource(pdfDoc, getClass());
			samplePDFFile = getFile(resource2);

			// ClassLoader cl = this.getClass().getClassLoader();
			// InputStream inputStream = cl.getResourceAsStream(gateDoc);

			// sampleAnnotatedFile = ResourceUtils.getFile(TestFiles.gateDoc);
			// log.info(sampleAnnotatedFile.getAbsolutePath());
			// samplePDFFile = ResourceUtils.getFile(TestFiles.pdfDoc);
			// log.info(samplePDFFile.getAbsolutePath());

		} catch (Exception e) {
			log.info("ERROR ON LOADING TEST FILES...");
		}
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

	private File getFile(ClassPathResource resource) throws Exception{
		File newFile = null;

		InputStream inputStream = resource.getInputStream();
		try {			
			newFile = File.createTempFile(resource.getFilename(), ".tmp");
			FileUtils.copyInputStreamToFile(inputStream, newFile);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return newFile;
	}
}
