package com.witcher.gallery.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witcher.gallery.models.dtos.PhotoDto;
import com.witcher.gallery.enums.Order;
import com.witcher.gallery.models.entities.Photo;
import com.witcher.gallery.services.PhotoService;
import com.witcher.gallery.util.MyMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final MyMapper myMapper;

    @Autowired
    public PhotoController(
            PhotoService photoService,
            ModelMapper modelMapper,
            ObjectMapper objectMapper,
            MyMapper orderMapper
    ) {
        this.photoService = photoService;

        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.myMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> getAllPhotos() {
        List<Photo> photos = this.photoService.findAllPhotos();
        List<PhotoDto> photoDtos = new ArrayList<>();

        photos.forEach(photo -> photoDtos.add(this.modelMapper.map(photo, PhotoDto.class)));

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(photoDtos, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/search")
    public ResponseEntity<HttpStatus> getSomePhotosWithWordsInTitle(
            @RequestParam(name="words") List<String> words
    ) {
        List<Photo> photos = this.photoService.findAllPhotosByWordsInTitle(words);
        ResponseEntity responseEntity = new ResponseEntity(photos, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/sorted")
    public ResponseEntity<HttpStatus> getAllPhotosSortedByTitle(
            @RequestParam(name="sorting", required = false) String order
    ) {
        Order defaultOrder;

        if(order == null)
            defaultOrder = Order.ASCENDING;
        else
            defaultOrder = this.myMapper.convertToOrder(order);


        List<Photo> photos = this.photoService.findAllPhotosSortedByTitle(defaultOrder);
        List<PhotoDto> photoDtos = new ArrayList();

        photos.forEach(photo -> photoDtos.add(this.modelMapper.map(photo, PhotoDto.class)));

        return new ResponseEntity(photoDtos, HttpStatus.OK);
    }

    @GetMapping("/{photo_id}")
    public ResponseEntity<HttpStatus> getPhotoById(
        @PathVariable("photo_id") String photoId
    ) {
        Photo photo = this.photoService.findPhotoById(photoId);
        PhotoDto photoDTO = this.modelMapper.map(photo, PhotoDto.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(photoDTO, HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<HttpStatus> postPhoto(
            @RequestParam("photo_details") String photoDetails, // looking for a way to automatically convert String photoDetails to PhotoDTO class
            @RequestParam("file") MultipartFile file
    ) {
        PhotoDto photoDTO = null;

        try {
            photoDTO = this.objectMapper.readValue(photoDetails, PhotoDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Photo photo = this.modelMapper.map(photoDTO, Photo.class);
        Photo savedPhoto = this.photoService.createPhoto(photo, file);
        PhotoDto savedPhotoDto = this.modelMapper.map(savedPhoto, PhotoDto.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(savedPhotoDto, HttpStatus.CREATED);

        return responseEntity;
    }

    @PutMapping(path = "/{photo_id}", consumes = {"multipart/form-data"})
    public ResponseEntity<HttpStatus> putPhoto(
            @PathVariable("photo_id") String photoId,
            @RequestParam("photo_details") String photoDetails,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) {
        PhotoDto photoDTO = null;

        try {
            photoDTO = this.objectMapper.readValue(photoDetails, PhotoDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Photo photo = this.modelMapper.map(photoDTO, Photo.class);
        Photo updatedPhoto = this.photoService.updatePhoto(photoId, photo, file);
        PhotoDto updatedPhotoDto = this.modelMapper.map(updatedPhoto, PhotoDto.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(updatedPhotoDto, HttpStatus.OK);

        return responseEntity;
    }

    @DeleteMapping("/{photo_id}")
    public ResponseEntity<HttpStatus> deletePhotoById(
            @PathVariable("photo_id") String photoId
    ) {
        this.photoService.deletePhotoById(photoId);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(true, HttpStatus.OK);

        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllPhotos() {
        this.photoService.deleteAllPhotos();
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(true, HttpStatus.OK);

        return responseEntity;
    }
}
