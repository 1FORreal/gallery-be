package com.witcher.gallery.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class PhotoDto {
    private String id;
    private String title;
    private String description;

    private FilePropertiesDto fileProperties;
    private Date creationDate;
    private Date lastModificationDate;

    public PhotoDto() {}

    @Override
    public String toString() {
        return "PhotoDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", filePropertiesDto=" + fileProperties +
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

    public FilePropertiesDto getFileProperties() {
        return fileProperties;
    }

    public void setFileProperties(FilePropertiesDto fileProperties) {
        this.fileProperties = fileProperties;
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
