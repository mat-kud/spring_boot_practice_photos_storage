package com.example.demo.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    private MultipartFile data;
    private String category;

    public MultipartFile getData() {
        return data;
    }

    public void setData(MultipartFile data) {
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
