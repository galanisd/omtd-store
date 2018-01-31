package eu.openminted.store.restclient;

import eu.openminted.store.common.StoreREST;
import eu.openminted.store.common.StoreResponse;
import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Class representing a corpus of publications.
 *
 * @author spyroukostas
 *
 */

public class Corpus {

    private int totalPublications;
    private int indexFrom;
    private int subsetSize;
    private String archiveId;
    private List<String> filepaths;
    private List<Publication> publications = null;
    private StoreRESTClient client;
    private String endpoint = "http://localhost:8080/"; // FIXME: move to property file.


    public Corpus(String archiveId) {
        this.archiveId = archiveId;

        // create a rest client
        client = new StoreRESTClient(endpoint);

        // retrieve all files inside the archive
        this.filepaths = client.listFiles(archiveId, false, true);
//        this.filepaths = listFilesInArchPostREST(archiveId, false, true);

        // analyze file-paths and create publication entries
        analyzeFilepaths();
//        printPublications(this.publications);

        // download metadata folder
        File save_to = new File("/tmp/corpus_metadata/" + archiveId + ".zip");
        System.out.println("Parent:" + save_to.getParentFile().toString());
        save_to.getParentFile().mkdirs();
        System.out.println("Save to:" + save_to.toString());
        StoreResponse response = client.downloadArchive(archiveId + "/metadata", save_to.toString());

        // parse metadata files and extract publication titles
        extractPublicationTitles(save_to.toString());
    }


    private void extractPublicationTitles(String metadataLocation) {
//        Path metadata_path = Paths.get(metadataLocation);
        File metadata_zip = new File(metadataLocation);
        File metadata_dir = new File(FilenameUtils.removeExtension(metadata_zip.toString())); // FIXME

        try {
            unzip(metadata_zip, metadata_dir.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        File[] file_list = metadata_dir.listFiles();
        for (File f : file_list) {
            System.out.println(f);
        }
//        metadata_dir.delete();
    }



    public static void unzip(File source, String out) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {

            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {

                File file = new File(out, entry.getName());

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();

                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    try {

                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                        byte[] buffer = new byte[1024];

                        int location;

                        while ((location = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, location);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                entry = zis.getNextEntry();
            }
        }
    }

//    Unzip unzipper = new Unzip();
//    unzipper.setSrc(theZIPFile);
//    unzipper.setDest(theTargetFolder);
//    unzipper.execute();

//    private List<String> listFilesInArchPostREST(String archiveId, boolean listDirs, boolean recursive) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add(StoreREST.archiveID, archiveId);
//        params.add(StoreREST.listDirectories, listDirs);
//        params.add(StoreREST.recursive, recursive);
//
//        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params);
//
//        String[] paths;
//        try {
//            paths = restTemplate.postForObject(endpoint + StoreREST.listFilesInArch, request, String[].class);
//            return new ArrayList<>(Arrays.asList(paths));
//        } catch (Exception e) {
//            System.out.println("ERROR: could not retrieve file list.");
//            System.out.println(e);
//        }
//        return null;
//    }


    private void analyzeFilepaths() {
        int abstractMask    = 0x0001;
        int fulltextMask    = 0x0010;
        int metadataMask    = 0x0100;
        int annotationsMask = 0x1000;

        List<String> files;
        if(filepaths == null) {
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

            if (file.getParent().getFileName().toString().equals("abstract")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + abstractMask);

            } else if (file.getParent().getFileName().toString().equals("fulltext")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + fulltextMask);

            } else if (file.getParent().getFileName().toString().equals("metadata")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + metadataMask);

            } else if (file.getParent().getFileName().toString().equals("annotations")) {
                publicationInfo.replace(filename, publicationInfo.get(filename) + annotationsMask);

            } else {
                //
            }
        }

        if (publications == null) {
            publications = new ArrayList<>();
        }

        // create publication entries
        Iterator it = publicationId.iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
            int value = publicationInfo.get(id);

            boolean hasAbstract = ((value & abstractMask)==abstractMask);
            boolean hasFulltext = ((value & fulltextMask)==fulltextMask);
            boolean hasMetadata = ((value & metadataMask)==metadataMask);
            boolean hasAnnotations = ((value & annotationsMask)==annotationsMask);

            publications.add(new Publication(id, id/*title here*/, this.archiveId,
                    hasFulltext, hasAbstract, hasMetadata, hasAnnotations));
        }

        setTotalPublications(publicationInfo.size());
    }

    public void printPublications(List<Publication> publications) {
        if (publications != null) {
            System.out.println("Printing Publication Info:");
            publications.forEach(pub -> pub.printPublicationInfo());
        }
    }


    public List<Publication> getCorpusSubset(int from, int size) {
        if (publications != null)

            if (publications.size() >= from+size)
                return this.getPublications().subList(from, (from+size));

            else if (publications.size() > from)
                return this.getPublications().subList(from, publications.size());

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


    public List<Publication> getPublications() {
        return publications;
    }

    protected void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

}
