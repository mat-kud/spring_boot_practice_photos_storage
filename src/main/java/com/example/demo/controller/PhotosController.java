package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.FileUploadRequest;
import com.example.demo.model.Photo;
import com.example.demo.service.PhotosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class PhotosController {

    private final PhotosService photosService;

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }


    @GetMapping("/photos")
    public Iterable<Photo> getAll(){
        return photosService.getAllPhotos();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/photos/{id}")
    public Photo get(@PathVariable int id){
        return photosService.getById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/photos/filter/category")
    public List<Photo> getByCategory(@RequestParam List<Category> values){
        return photosService.getByCategory(values);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/photos/{id}")
    public void delete(@PathVariable int id){
        photosService.remove(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Photo create(@ModelAttribute FileUploadRequest uploadRequest) throws IOException {
        String fileName = uploadRequest.getData().getOriginalFilename();
        String contentType = uploadRequest.getData().getContentType();
        String category = uploadRequest.getCategory();
        byte[] data = uploadRequest.getData().getBytes();
        LocalDateTime dateCreated = uploadRequest.getDateCreated();
        return photosService.save(fileName, contentType, category, data, dateCreated);
    }
}