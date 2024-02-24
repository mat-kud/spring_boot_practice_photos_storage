package com.example.demo.controller;

import com.example.demo.service.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class DownloadController {

    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable int id) throws IOException {
        return downloadService.download(id);
    }
}