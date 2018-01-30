package eu.openminted.store.restclient;

import eu.openminted.store.common.StoreREST;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing a corpus (or a subset of a corpus) of publications.
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
//    private RestTemplate restTemplate;
    private String endpoint = "http://localhost:8080/"; // FIXME: move to property file.

    public Corpus(String archiveId) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add(StoreREST.archiveID, archiveId);
        params.add(StoreREST.listDirectories, false);
        params.add(StoreREST.recursive, true);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params);

        String[] paths = restTemplate.postForObject(endpoint + StoreREST.listFilesInArch, request, String[].class);

        this.filepaths = new ArrayList<>(Arrays.asList(paths));
        this.archiveId = archiveId;
        this.publications = new ArrayList<>();

        analyzeFilepaths();
//        printPublications(this.publications);
    }
    

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
            for (Publication pub : publications) {
                System.out.println("\n");
                System.out.println("Archive ID:\t" + pub.getArchiveId());
                System.out.println("Pub ID:  \t" + pub.getId());
                System.out.println("Title:   \t" + pub.getTitle());
                System.out.println("Abstract:\t" + pub.isHasAbstract());
                System.out.println("Fulltext:\t" + pub.isHasFulltext());
                System.out.println("Metadata:\t" + pub.isHasMetadata());
                System.out.println("Annotations:\t" + pub.isHasAnnotations());
            }
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
