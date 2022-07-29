package com.witcher.gallery.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class PhotoDTO {
    private String id;
    private String title;
    private String description;
    private String filename;
    private Long filesize;
    private Date creationDate;
    private Date lastModificationDate;

    public PhotoDTO() {}

    @Override
    public String toString() {
        return "PhotoDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", filename='" + filename + '\'' +
                ", filesize='" + filesize + '\'' +
                ", creationDate=" + creationDate +
                ", lastModificationDate=" + lastModificationDate +
                '}';
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFilesize() {
        return filesize;
    }

    @JsonIgnore
    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}
