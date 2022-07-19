package com.witcher.gallery.services;

import com.witcher.gallery.enums.Order;
import com.witcher.gallery.models.Photo;
import com.witcher.gallery.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final FileService fileService;

    @Autowired
    public PhotoService(
            PhotoRepository photoRepository,
            FileService fileService
    ) {
        this.photoRepository = photoRepository;
        this.fileService = fileService;
    }

    public List<Photo> findAllPhotos() {
        List<Photo> photos = new ArrayList();

        this.photoRepository.findAll().iterator().forEachRemaining(photo -> photos.add(photo));
        return photos;
    }


    public List<Photo> findAllPhotosSortedByTitle(Order order) {
        Sort sortingOrder = Sort.by("title");

        if(order.equals(order.ASCENDING))
            sortingOrder = sortingOrder.ascending();
        else
            sortingOrder = sortingOrder.descending();

        List<Photo> photos = this.photoRepository.findAll(sortingOrder);

        return photos;
    }

    public Photo findPhotoById(String photoId) {
        Optional<Photo> photoOptional = this.photoRepository.findById(photoId);

        if(photoOptional.isPresent())
            return photoOptional.get();

        throw new RuntimeException("Photo not found!");
    }

    public Photo createPhoto(Photo photo, MultipartFile file) {

        String fileId = this.fileService.storeFile(file);
        photo.setFileId(fileId);

        Photo savedPhoto = this.photoRepository.save(photo);

        return savedPhoto;
    }

    public Photo updatePhoto(String photoId, Photo photo, MultipartFile file) {
        Photo toModify = this.findPhotoById(photoId);

        if(file != null) {
            this.fileService.deleteFileById(toModify.getFileId());
            String fileId = this.fileService.storeFile(file);
            photo.setFileId(fileId);
        }

        if(!toModify.getTitle().equals(photo.getTitle()))
            toModify.setTitle(photo.getTitle());

        if(!toModify.getDescription().equals(photo.getDescription()))
            toModify.setDescription(photo.getDescription());

        if(photo.getFileId() != null && !toModify.getFileId().equals(photo.getFileId()))
            toModify.setFileId(photo.getFileId());

        Photo modifiedPhoto = this.photoRepository.save(toModify);

        return modifiedPhoto;
    }

    public void deletePhotoById(String photoId) {
        Photo photo = this.findPhotoById(photoId);

        this.photoRepository.deleteById(photoId);
        this.fileService.deleteFileById(photo.getFileId());
    }

    public void deleteAllPhotos() {
        this.photoRepository.deleteAll();
        fileService.deleteAllFiles();
    }
}
