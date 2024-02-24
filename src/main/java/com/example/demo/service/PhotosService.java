package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Category;
import com.example.demo.model.FileUploadRequest;
import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PhotosService {
    private final PhotosRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotosService.class);

    public PhotosService(PhotosRepository repository) {
        this.repository = repository;
    }

    public Iterable<Photo> getAllPhotos() {
        LOGGER.info("Getting all photos");
        return repository.findAll();
    }

    public Photo getById(int id) {
        Photo photo =  repository.findById(id).orElse(null);
        if(photo == null){
            LOGGER.error(String.format("Photo not found, id=%d", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Photo with id=%d not found", id));
        }
        LOGGER.info(String.format("Getting the photo, id=%d", id));
        return photo;
    }

    public List<Photo> getByCategory(List<Category> values){
        List<Photo> categoryList =  repository.getByCategory(values);
        if(categoryList.isEmpty()){
            LOGGER.error(String.format("Photos not found, categories=%s", values.toString()));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photos with such categories not found");
        }
        LOGGER.info(String.format("Getting photos, categories=%s", values.toString()));
        return categoryList;
    }

    public void remove(int id) {
        LOGGER.info(String.format("Removing the photo, id=%s", id));
        repository.deleteById(id);
    }

    public Photo save(String fileName, String contentType, String category, byte[] data, LocalDateTime dateCreated) throws IOException {
        if(!checkIfFileIsImage(Objects.requireNonNull(contentType))){
            LOGGER.error(String.format("%s file format provided but expected an image", contentType));
            throw new BadRequestException("File " + fileName + " is not an image");
        }
        LOGGER.info("Adding the photo");
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setContentType(contentType);
        photo.setCategory(Category.valueOf(category.toUpperCase()));
        photo.setDateCreated(dateCreated);

        repository.save(photo);
        return photo;
    }

    private boolean checkIfFileIsImage(String contentType){
        return contentType.matches("image/[a-zA-Z]{1,5}");
    }
}
