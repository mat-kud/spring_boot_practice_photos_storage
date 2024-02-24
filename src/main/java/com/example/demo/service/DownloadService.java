package com.example.demo.service;

import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;

@Service
public class DownloadService {
    private final PhotosRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    public DownloadService(PhotosRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<byte[]> download(int id) throws IOException {
        Photo photo = repository.findById(id).orElse(null);
        if(photo == null){
            LOGGER.error(String.format("Photo not found, id=%d", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        String filePath = "src/data/photos/" + photo.getCategory() + "/" + photo.getFileName();
        File fi = new File(filePath);
        byte[] data = readAllBytes(fi.toPath());

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