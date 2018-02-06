package eu.openminted.store.restclient;

/**
 * A class used to represent a publication inside a Corpus.
 *
 * @author spyroukostas
 */

public class PublicationInfo {

    private String id;
    private String title;
    private String archiveId;
    private boolean hasFulltext = false;
    private boolean hasAbstract = false;
    private boolean hasMetadata = false;
    private boolean hasAnnotations = false;

    public PublicationInfo(String id, String archiveId) {
        this.id = id;
        this.title = id;
        this.archiveId = archiveId;
    }

    public PublicationInfo(String id, String title, String archiveId) {
        this.id = id;
        this.title = title;
        this.archiveId = archiveId;
    }

    public PublicationInfo(String id, String title, String archiveId, boolean hasFulltext,
                           boolean hasAbstract, boolean hasMetadata, boolean hasAnnotations) {
        this.id = id;
        this.title = title;
        this.archiveId = archiveId;
        this.hasFulltext = hasFulltext;
        this.hasAbstract = hasAbstract;
        this.hasMetadata = hasMetadata;
        this.hasAnnotations = hasAnnotations;
    }


    public void printPublicationInfo() {
        System.out.println("\n");
        System.out.println("Archive ID:\t" + this.getArchiveId());
        System.out.println("Pub ID:  \t" + this.getId());
        System.out.println("Title:   \t" + this.getTitle());
        System.out.println("Abstract:\t" + this.isHasAbstract());
        System.out.println("Fulltext:\t" + this.isHasFulltext());
        System.out.println("Metadata:\t" + this.isHasMetadata());
        System.out.println("Annotations:\t" + this.isHasAnnotations());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public boolean isHasFulltext() {
        return hasFulltext;
    }

    public void setHasFulltext(boolean hasFulltext) {
        this.hasFulltext = hasFulltext;
    }

    public boolean isHasAbstract() {
        return hasAbstract;
    }

    public void setHasAbstract(boolean hasAbstract) {
        this.hasAbstract = hasAbstract;
    }

    public boolean isHasMetadata() {
        return hasMetadata;
    }

    public void setHasMetadata(boolean hasMetadata) {
        this.hasMetadata = hasMetadata;
    }

    public boolean isHasAnnotations() {
        return hasAnnotations;
    }

    public void setHasAnnotations(boolean hasAnnotations) {
        this.hasAnnotations = hasAnnotations;
    }

}
