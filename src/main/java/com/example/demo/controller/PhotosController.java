package com.example.demo.controller;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Category;
import com.example.demo.model.FileUploadRequest;
import com.example.demo.model.Photo;
import com.example.demo.service.PhotosService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class PhotosController {

    private final PhotosService photosService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotosController.class);

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }


    @GetMapping("/photos")
    public Iterable<Photo> getAll(){
        LOGGER.info("Getting all photos");
        return photosService.getAllPhotos();
    }

    @GetMapping("/photos/{id}")
    public Photo get(@PathVariable int id){
        Photo photo = photosService.getById(id);
        if(photo == null){
            LOGGER.error(String.format("Photo not found, id=%d", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Photo with id=%d not found", id));
        }
        LOGGER.info(String.format("Getting the photo, id=%d", id));

        return photo;
    }

    @GetMapping("/photos/filter/category")
    public List<Photo> getByCategory(@RequestParam List<Category> values){
        List<Photo> list  =  photosService.getByCategory(values);
        if(list.isEmpty()){
            LOGGER.error(String.format("Photos not found, categories=%s", values.toString()));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photos with such categories not found");
        }
        LOGGER.info(String.format("Getting photos, categories=%s", values.toString()));
        return list;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/photos/{id}")
    public void delete(@PathVariable int id){
        LOGGER.info(String.format("Removing the photo, id=%s", id));
        photosService.remove(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Photo create(@ModelAttribute FileUploadRequest uploadRequest) throws IOException {
        if(!checkIfFileIsImage(Objects.requireNonNull(uploadRequest.getData().getContentType()))){
            LOGGER.error(String.format("%s file format provided but expected an image", uploadRequest.getData().getContentType()));
            throw new BadRequestException("File " + uploadRequest.getData().getOriginalFilename() + " is not an image");
        }
        LOGGER.info("Adding the photo");
        return photosService.save(uploadRequest.getData().getOriginalFilename(),
                uploadRequest.getData().getContentType(),
                uploadRequest.getCategory(),
                uploadRequest.getData().getBytes(),
                uploadRequest.getDateCreated());
    }

    private boolean checkIfFileIsImage(String contentType){
        return contentType.matches("image/[a-zA-Z]{1,5}");
    }


}
