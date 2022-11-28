package com.witcher.gallery.controllers;

import com.witcher.gallery.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/files")
public class FileController {
    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<HttpStatus> getFile(
            @PathVariable("filename") String filename
    ) {
        Resource resource = storageService.loadFile(filename);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type","image/jpeg");

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(resource, responseHeaders, HttpStatus.OK);

        return responseEntity;
    }

}
