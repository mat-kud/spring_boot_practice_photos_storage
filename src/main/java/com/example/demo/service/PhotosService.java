package com.example.demo.service;

import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.springframework.stereotype.Service;

@Service
public class PhotosService {
    private final PhotosRepository repository;

    public PhotosService(PhotosRepository repository) {
        this.repository = repository;
    }

    public Iterable<Photo> getAll() {
        return repository.findAll();
    }

    public Photo get(int id) {
        return repository.findById(id).orElseThrow(null);
    }

    public void remove(int id) {
        repository.deleteById(id);
    }

    public Photo save(String fileName, String contentType, byte[] data) {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setContentType(contentType);
        photo.setData(data);
        repository.save(photo);
        return photo;
    }
}
