package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PhotosService {
    private final PhotosRepository repository;

    public PhotosService(PhotosRepository repository) {
        this.repository = repository;
    }

    public Iterable<Photo> getAllPhotos() {
        return repository.findAll();
    }

    public Photo getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Photo> getByCategory(List<Category> categoryList){
        return repository.getByCategory(categoryList);
    }

    public void remove(int id) {
        repository.deleteById(id);
    }

    public Photo save(String fileName, String contentType, String category, byte[] data, LocalDateTime dateCreated)  {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setContentType(contentType);
        photo.setCategory(Category.valueOf(category.toUpperCase()));
        photo.setData(data);
        photo.setDateCreated(dateCreated);

        repository.save(photo);
        return photo;
    }
}
