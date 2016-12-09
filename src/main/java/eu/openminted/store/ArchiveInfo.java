package eu.openminted.store;

import java.util.List;

class ArchiveInfo {
	
    private String id;
    private List<ArchiveInfo> subArchives;
    private List<String> files;

    public ArchiveInfo(String id) {
        this.id = id;
    }

    public ArchiveInfo(String id, List<ArchiveInfo> subArchives, List<String> files) {
        this.id = id;
        this.subArchives = subArchives;
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ArchiveInfo> getSubArchives() {
        return subArchives;
    }

    public void setSubArchives(List<ArchiveInfo> subArchives) {
        this.subArchives = subArchives;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
