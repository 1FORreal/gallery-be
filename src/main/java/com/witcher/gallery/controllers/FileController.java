package com.witcher.gallery.controllers;

import com.witcher.gallery.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{file_id}")
    public ResponseEntity<HttpStatus> getFile(
            @PathVariable("file_id") String fileId
    ) {
        InputStreamResource resource = new InputStreamResource(this.fileService.load(fileId));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type","image/jpeg");

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(resource, responseHeaders, HttpStatus.OK);

        return responseEntity;
    }

}
