package com.witcher.gallery.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FilePropertiesDto {

    private String filename;

    @JsonIgnore
    private Long filesize;

    @Override
    public String toString() {
        return "FilePropertiesDto{" +
                "filename='" + filename + '\'' +
                ", filesize=" + filesize +
                '}';
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

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }
}
