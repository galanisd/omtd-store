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
    private List<Publication> set;
    private RestTemplate restTemplate;
    private String endpoint = "http://localhost:8080/"; // FIXME: move to property file.

    public Corpus(String archiveId) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add(StoreREST.archiveID, archiveId);
        params.add(StoreREST.listDirectories, false);
        params.add(StoreREST.recursive, true);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params);

        String[] paths = restTemplate.postForObject(endpoint + StoreREST.listFilesInArch, request, String[].class);

        filepaths = new ArrayList<>(Arrays.asList(paths));
        analyzeFilepaths();
    }
    

    private void analyzeFilepaths() {
        List<String> files;
        if(filepaths == null) {
            totalPublications = 0;
            return;
        }
        files = filepaths.stream()
                .map(Paths::get)
                .map(Path::getFileName)
                .map(Path::toString)
                .map(FilenameUtils::removeExtension)
                .collect(Collectors.toList());

        Set<String> publicationNames = new HashSet<>(files);

        totalPublications = publicationNames.size();
        System.out.println(files);
        System.out.println("Total files: " + filepaths.size());
        System.out.println("Total publications: " + getTotalPublications());
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

    public int getIndexFrom() {
        return indexFrom;
    }

    protected void setIndexFrom(int indexFrom) {
        this.indexFrom = indexFrom;
    }

    public int getSubsetSize() {
        return subsetSize;
    }

    protected void setSubsetSize(int subsetSize) {
        this.subsetSize = subsetSize;
    }

    public List<Publication> getSet() {
        return set;
    }

    protected void setSet(List<Publication> set) {
        this.set = set;
    }

}
