package com.example.demo.controller;

import com.example.demo.model.Photo;
import com.example.demo.service.PhotosService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class DownloadController {

    private final PhotosService photosService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    public DownloadController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable int id){
        Photo photo = photosService.getById(id);
        if(photo == null){
            LOGGER.error(String.format("Photo not found, id=%d", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        byte[] data = photo.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(photo.getContentType()));
        ContentDisposition build = ContentDisposition.builder("attachment")
                .filename(photo.getFileName())
                .build();
        headers.setContentDisposition(build);
        LOGGER.info(String.format("Downloading the photo, name=%s", photo.getFileName()));
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
