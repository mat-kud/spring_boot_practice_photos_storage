package com.example.demo.controller;

import com.example.demo.model.FileUploadRequest;
import com.example.demo.model.Photo;
import com.example.demo.service.PhotosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class PhotosController {

    private final PhotosService photosService;

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }


    @GetMapping("/photos")
    public Iterable<Photo> getAll(){
        return photosService.getAll();
    }

    @GetMapping("/photos/{id}")
    public Photo get(@PathVariable int id){
        Photo photo = photosService.getById(id);
        if(photo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return photo;
    }

    @DeleteMapping("/photos/{id}")
    public void delete(@PathVariable int id){
        photosService.remove(id);
    }

    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Photo create(@ModelAttribute FileUploadRequest uploadRequest) throws IOException {
        return photosService.save(uploadRequest.getData().getOriginalFilename(),
                uploadRequest.getData().getContentType(),
                uploadRequest.getCategory(),
                uploadRequest.getData().getBytes());
    }


}
