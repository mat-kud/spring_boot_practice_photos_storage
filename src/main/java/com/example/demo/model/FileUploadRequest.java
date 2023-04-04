package com.example.demo.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FileUploadRequest {
    @NotNull
    private MultipartFile data;
    private String category;
    private LocalDateTime dateCreated;

    public FileUploadRequest() {
        this.dateCreated = LocalDateTime.now();
    }

    public MultipartFile getData() {
        return data;
    }

    public void setData(MultipartFile data) {
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
