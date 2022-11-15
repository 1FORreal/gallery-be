package com.witcher.gallery.models.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FileProperties {

    @Column(name = "file_name", updatable = true, nullable = false)
    private String filename;

    @Column(name = "file_size", updatable = true, nullable = false)
    private Long filesize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }
}
