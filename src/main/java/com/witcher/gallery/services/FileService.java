package com.witcher.gallery.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final File directory;

    public FileService() {
        directory = new File("D:\\Programming\\gallery\\gallery\\src\\main\\resources\\imgs");
    }

    public void init() {}

    public File findFile(String fileId) {
        List<String> filenames = List.of(this.directory.list());

        for(String filename : filenames)
            if(filename.contains(fileId))
                return new File(directory.getPath() + "\\" + filename);

        throw new RuntimeException("File not found!");
    }

    public String storeFile(MultipartFile file){
        if(!FilenameUtils.isExtension(file.getOriginalFilename(), List.of("svg","jpg")))
            throw new RuntimeException("Invalid file extension");

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        String fileUUID = UUID.randomUUID().toString();
        String filename = fileUUID + "." + extension;

        try {
            file.transferTo(new File(directory.getPath() + "\\" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileUUID;
    }

    public FileInputStream load(String fileId) {
        File file = this.findFile(fileId);
        FileInputStream resource = null;

        try {
            resource = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return resource;
    }

    public void deleteFileById(String fileId) {
        File file = this.findFile(fileId);

        Boolean isDeleted = file.delete();

        if(!isDeleted)
            throw new RuntimeException("FileService error");
    }

    public void deleteAllFiles() {
        List<String> filenames = List.of(this.directory.list());

        for(String filename : filenames) {
            File file = new File(directory.getPath() + "\\" + filename);
            Boolean isDeleted = file.delete();

            if(!isDeleted)
                throw new RuntimeException("FileService error!");
        }
    }


}
