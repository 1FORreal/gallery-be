package com.witcher.gallery.services;

import com.witcher.gallery.models.entities.FileProperties;
import com.witcher.gallery.models.entities.Photo;
import com.witcher.gallery.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final static Sort DEFAULT_SORT = Sort.by("creationDate").ascending();

    @Autowired
    public PhotoService(
            PhotoRepository photoRepository,
            StorageService storageService
    ) {
        this.photoRepository = photoRepository;
        this.storageService = storageService;
    }

    public List<Photo> findAllPhotos(
            Integer pageNumber,
            Integer pageSize,
            List<String> sortProperties
    ) {
        Sort sort = this.constructSort(sortProperties);

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Photo> pageResult = this.photoRepository.findAll(pageRequest);

        if(!pageResult.hasContent()) return new ArrayList<Photo>();
        return pageResult.getContent();
    }

    public Photo findPhotoById(String photoId) {
        Optional<Photo> photoOptional = this.photoRepository.findById(photoId);

        if(photoOptional.isPresent())
            return photoOptional.get();

        throw new RuntimeException("Photo not found!");
    }

    public Photo createPhoto(Photo photo, MultipartFile file) {
        String filename = this.storageService.storeFile(file);

        FileProperties fileProperties = new FileProperties();
        fileProperties.setFilename(filename);
        fileProperties.setFilesize(file.getSize());
        photo.setFileProperties(fileProperties);

        Photo savedPhoto = this.photoRepository.save(photo);

        return savedPhoto;
    }

    public Photo updatePhoto(String photoId, Photo photo, MultipartFile file) {
        if(!photoId.equals(photo.getId()))
            throw new RuntimeException("Photo id mismatches the id of the entity!");

        Photo toModify = this.findPhotoById(photoId);

        if(file != null) {
            this.storageService.deleteFile(toModify.getFileProperties().getFilename());
            String filename = this.storageService.storeFile(file);
            photo.getFileProperties().setFilename(filename);
            toModify.getFileProperties().setFilesize(file.getSize());
        }

        if(!toModify.getTitle().equals(photo.getTitle()))
            toModify.setTitle(photo.getTitle());

        if(!toModify.getDescription().equals(photo.getDescription()))
            toModify.setDescription(photo.getDescription());

        if(photo.getFileProperties().getFilename() != null && !toModify.getFileProperties().getFilename().equals(photo.getFileProperties().getFilename()))
            toModify.getFileProperties().setFilename(photo.getFileProperties().getFilename());

        Photo modifiedPhoto = this.photoRepository.save(toModify);

        return modifiedPhoto;
    }

    public void deletePhotoById(String photoId) {
        Photo photo = this.findPhotoById(photoId);

        this.photoRepository.deleteById(photoId);
        this.storageService.deleteFile(photo.getFileProperties().getFilename());
    }

    public void deleteAllPhotos() {
        this.photoRepository.deleteAll();
        storageService.deleteAllFiles();
    }

    private Sort constructSort(List<String> sortProperties) {
        if(sortProperties == null || sortProperties.size() <= 0) return DEFAULT_SORT;

        Sort sort = Sort.unsorted();
        for(String s : sortProperties) {
            Integer separatorIndex = (s.indexOf('_') == -1 ? s.length() : s.indexOf('_'));
            String property = s.substring(0, separatorIndex);
            String sortDirection = s.substring((separatorIndex + 1 > s.length() ? null : separatorIndex + 1));
            Sort.Direction direction = (sortDirection == null || sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);

            sort = sort.and(Sort.by(direction, property));
        }

        return sort;
    }
}
