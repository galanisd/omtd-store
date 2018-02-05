package eu.openminted.store.restclient;

import eu.openminted.store.common.StoreResponse;
import eu.openminted.utils.files.ZipToDir;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing a corpus of publications.
 *
 * @author spyroukostas
 */

public class Corpus {

    private int totalPublications;
    private int indexFrom;
    private int subsetSize;
    private String archiveId;
    private List<String> filepaths;
    private List<PublicationInfo> publications = null;
    private StoreRESTClient client; // FIXME: make class static?
    private String endpoint = "http://localhost:8080/"; // FIXME: get value from property file.


    public Corpus(String archiveId) {
        this.archiveId = archiveId;

        // create a rest client
        client = new StoreRESTClient(endpoint);

        // retrieve all files inside the archive
        this.filepaths = client.listFiles(archiveId, false, true);  // FIXME: add 'ignoreZipFiles' argument.
//        this.filepaths = listFilesInArchPostREST(archiveId, false, true);

        // analyze file-paths and create publication entries
        createPublicationEntries();


        printPublications(this.publications);


//        System.out.println(extractPublicationTitles(archiveId));
    }


    /**
     * Downloads metadata files to extract publication titles.
     * Returns a HashMap of Publication IDs and Publication Titles.
     *
     * @param archiveId
     * @returns {@link HashMap<String, String>}
     */
    private HashMap<String, String> extractPublicationTitles(String archiveId) {

        File metadata_zip = new File(System.getProperty("java.io.tmpdir") + "/corpus_metadata/" + archiveId + ".zip");
        File metadata_dir = new File(FilenameUtils.removeExtension(metadata_zip.toString()) + "/metadata");
//        System.out.println("Parent:" + metadata_zip.getParentFile().toString());

        if (!metadata_dir.exists()) {

            // download metadata folder as zip
            metadata_zip.getParentFile().mkdirs();
            System.out.println("Downloading: " + metadata_zip.toString());
//            StoreResponse response = client.downloadArchive(archiveId + "/metadata", metadata_zip.toString()); // FIXME: response?
            StoreResponse response = client.fetchMetadata(archiveId, metadata_zip.toString()); // FIXME: response?

            try {
                File save_dir = new File(metadata_zip.getParent().toString());
                ZipToDir.unpackToWorkDir(metadata_zip, save_dir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally { // delete zip file
                try {
                    metadata_zip.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        HashMap<String, String> publication_titles = new HashMap<>();
        String title;
        String id;
        try {
            File[] file_list = metadata_dir.listFiles();
            for (File f : file_list) {
                if (!(title = extractTitleFromXML(f.toString())).equals("")) {
                    id = FilenameUtils.removeExtension(f.getName());
                    publication_titles.put(id, title);
//                    System.out.println("file: " + FilenameUtils.removeExtension(f.getName()) + "\n\tTitle: " + title);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        metadata_dir.delete();
        return publication_titles;
    }


    /**
     * Parses an XML file of a Publication and retrieves the Title.
     *
     * @param filename
     * @return {@link String}
     */
    private String extractTitleFromXML(String filename) {
        File xml = new File(filename);
        String title = "";
//        List<String > titles = new ArrayList<>();
        if (!xml.exists()) {
            return title;
//            return titles;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;

            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xml);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile("//*[local-name()='publication']//*[local-name()='title']");

            title = expr.evaluate(document);

/* TODO: uncomment and edit to retrieve all titles
            Object result = expr.evaluate(document, XPathConstants.NODE);

//            System.out.println("XML doc value: " + result.toString());

            //Iterate over results
            NodeList nodes = (NodeList) result;


            if (nodes != null) {
                System.out.println("Number of titles: " + nodes.getLength());  // FIXME: does not retrieve multiple titles.
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i) != null) {
                        titles.add(nodes.item(i).getNodeValue());
                        System.out.println("XML doc value: i = " + i + " :: " + (nodes.item(i).getNodeValue()));
                    } else {
                        System.out.println("Something is wrong: i = " + i + " :: " + (result.toString()));
                    }
                }
            } else {
                // return no title
            }
*/

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return title;
//        return titles;
    }


    /**
     * Populates the list of Publications.
     *
     */
    private void createPublicationEntries() {
        int abstractMask = 0x0001;
        int fulltextMask = 0x0010;
        int metadataMask = 0x0100;
        int annotationsMask = 0x1000;

        List<String> files;
        if (filepaths == null) {
            totalPublications = 0;
            return;
        }

        List<Path> paths = filepaths.stream()
                .map(Paths::get)
                .collect(Collectors.toList());

        // save for each publication the name (String) and a value (int)
        // which represents the existence/absence of abstract, fulltext, metadata and annotations.
        HashMap<String, Integer> publicationInfo = new HashMap<>();

        // create a set with publication IDs extracted from the filenames.
        Set<String> publicationId = new HashSet<>();

        for (Path file : paths) {
            String filename = file.getFileName().toString();
            filename = FilenameUtils.removeExtension(filename);
            publicationId.add(filename);
            publicationInfo.putIfAbsent(filename, 0x0000);

            String parent = file.getParent().getFileName().toString();

            if (parent.equals("abstract")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + abstractMask);

            } else if (parent.equals("fulltext")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + fulltextMask);

            } else if (parent.equals("metadata")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + metadataMask);

            } else if (parent.equals("annotations")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + annotationsMask);

            } else {
                //
            }
        }

        if (publications == null) {
            publications = new ArrayList<>();
        }

        // download and parse metadata files to extract publication titles
        HashMap<String, String> publication_titles = extractPublicationTitles(this.archiveId);

        // create publication entries
        Iterator it = publicationId.iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
            int value = publicationInfo.get(id);
            String title = publication_titles.get(id);
            if (title != null && !title.equals("")) {
                System.out.println("Title: " + title);
            } else {
                title = id;
            }

            boolean hasAbstract = ((value & abstractMask) == abstractMask);
            boolean hasFulltext = ((value & fulltextMask) == fulltextMask);
            boolean hasMetadata = ((value & metadataMask) == metadataMask);
            boolean hasAnnotations = ((value & annotationsMask) == annotationsMask);

            publications.add(new PublicationInfo(id, title, this.archiveId,
                    hasFulltext, hasAbstract, hasMetadata, hasAnnotations));
        }

        setTotalPublications(publicationInfo.size());
    }

    /**
     * Prints the {@link List<PublicationInfo>} that is given as an argument.
     *
     * @param publications
     */
    public void printPublications(List<PublicationInfo> publications) {
        if (publications != null) {
            System.out.println("Printing Publication Info:");
            publications.forEach(pub -> pub.printPublicationInfo());
        }
    }


    /**
     * Returns a subset of information of publications.
     *
     * @param from
     * @param size
     * @return {@link List<PublicationInfo>}
     */
    public List<PublicationInfo> getCorpusSubset(int from, int size) {
        if (publications != null)

            if (publications.size() >= from + size)
                return this.getPublicationsInfo().subList(from, (from + size));

            else if (publications.size() > from)
                return this.getPublicationsInfo().subList(from, publications.size());

        return null;
    }


    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }


    public int getTotalPublications() {
        return totalPublications;
    }

    protected void setTotalPublications(int totalPublications) {
        this.totalPublications = totalPublications;
    }


    public List<PublicationInfo> getPublicationsInfo() {
        return publications;
    }

    protected void setPublicationsInfo(List<PublicationInfo> publications) {
        this.publications = publications;
    }

}
