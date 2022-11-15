package com.witcher.gallery.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StorageService {
    private final Path rootLocation;
    private final List<String> allowedFileExtensions;


    public StorageService(
            @Value("${storage.location:/uploads}") final String stringLocation,
            @Value("${storage.allowed-file-extensions}") final String[] allowedFileExtensionsArray
    ) {
        this.rootLocation = Paths.get(stringLocation);
        this.allowedFileExtensions = List.of(allowedFileExtensionsArray);

        this.init();
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    private String generateRandomFilenameUUID(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        String fileUUID = UUID.randomUUID().toString();
        String filename = fileUUID + "." + extension;

        return filename;
    }

    public String storeFile(MultipartFile file){
        if(file.isEmpty()) throw new RuntimeException("Invalid file content!");

        if(file.getOriginalFilename().contains("..")) throw new RuntimeException("Invalid original filename!");

        if(!FilenameUtils.isExtension(file.getOriginalFilename(), this.allowedFileExtensions)) throw new RuntimeException("Invalid file extension");

        String filename = this.generateRandomFilenameUUID(file);

        try (InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, this.rootLocation.resolve(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    public Resource loadFile(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable())
                return  resource;
            else
                throw new FileNotFoundException("Could not find file " + filename);
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Something happened!");
    }

    public void deleteFile(String filename) {
        File file = new File(this.rootLocation.resolve(filename).toString());

        if(file.exists())
            if(file.delete()) return;

        else throw new RuntimeException("File not found!");
    }

    public void deleteAllFiles() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        this.init();
    }


}
