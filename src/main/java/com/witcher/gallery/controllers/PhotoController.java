package com.witcher.gallery.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witcher.gallery.dtos.PhotoDTO;
import com.witcher.gallery.enums.Order;
import com.witcher.gallery.models.Photo;
import com.witcher.gallery.services.FileService;
import com.witcher.gallery.services.PhotoService;
import com.witcher.gallery.util.OrderMapper;
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
    private final OrderMapper orderMapper;

    @Autowired
    public PhotoController(
            PhotoService photoService,
            ModelMapper modelMapper,
            ObjectMapper objectMapper,
            OrderMapper orderMapper
    ) {
        this.photoService = photoService;

        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> getAllPhotos() {
        List<Photo> photos = this.photoService.findAllPhotos();
        List<PhotoDTO> photoDTOs = new ArrayList<>();

        photos.forEach(photo -> photoDTOs.add(this.modelMapper.map(photo, PhotoDTO.class)));

        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(photoDTOs, HttpStatus.OK);

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
            defaultOrder = this.orderMapper.convertToOrder(order);


        List<Photo> photos = this.photoService.findAllPhotosSortedByTitle(defaultOrder);
        List<PhotoDTO> photoDTOs = new ArrayList();

        photos.forEach(photo -> photoDTOs.add(this.modelMapper.map(photo, PhotoDTO.class)));

        return new ResponseEntity(photoDTOs, HttpStatus.OK);
    }

    @GetMapping("/{photo_id}")
    public ResponseEntity<HttpStatus> getPhotoById(
        @PathVariable("photo_id") String photoId
    ) {
        Photo photo = this.photoService.findPhotoById(photoId);
        PhotoDTO photoDTO = this.modelMapper.map(photo, PhotoDTO.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(photoDTO, HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<HttpStatus> postPhoto(
            @RequestParam("photo_details") String photoDetails, // looking for a way to automatically convert String photoDetails to PhotoDTO class
            @RequestParam("file") MultipartFile file
    ) {
        PhotoDTO photoDTO = null;

        try {
            photoDTO = this.objectMapper.readValue(photoDetails, PhotoDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Photo photo = this.modelMapper.map(photoDTO, Photo.class);
        Photo savedPhoto = this.photoService.createPhoto(photo, file);
        PhotoDTO savedPhotoDTO = this.modelMapper.map(savedPhoto, PhotoDTO.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(savedPhotoDTO, HttpStatus.CREATED);

        return responseEntity;
    }

    @PutMapping(path = "/{photo_id}", consumes = {"multipart/form-data"})
    public ResponseEntity<HttpStatus> putPhoto(
            @PathVariable("photo_id") String photoId,
            @RequestParam("photo_details") String photoDetails,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) {
        PhotoDTO photoDTO = null;

        try {
            photoDTO = this.objectMapper.readValue(photoDetails, PhotoDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Photo photo = this.modelMapper.map(photoDTO, Photo.class);
        Photo updatedPhoto = this.photoService.updatePhoto(photoId, photo, file);
        PhotoDTO updatedPhotoDTO = this.modelMapper.map(updatedPhoto, PhotoDTO.class);
        ResponseEntity<HttpStatus> responseEntity = new ResponseEntity(updatedPhotoDTO, HttpStatus.OK);

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
