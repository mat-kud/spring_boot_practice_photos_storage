package com.example.demo.controller;

import com.example.demo.model.Photo;
import com.example.demo.service.PhotosService;
import org.springframework.http.HttpStatus;
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
        Photo photo = photosService.get(id);
        if(photo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return photo;
    }

    @DeleteMapping("/photos/{id}")
    public void delete(@PathVariable int id){
        photosService.remove(id);
    }

    @PostMapping("/photos")
    public Photo create(@RequestPart("data") MultipartFile file) throws IOException {
        return photosService.save(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }


}
