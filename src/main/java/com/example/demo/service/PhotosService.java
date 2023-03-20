package com.example.demo.service;

import com.example.demo.model.Photo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhotosService {
    private Map<String, Photo> db = new HashMap<>(){{
        put("1", new Photo(1, "hello.jpg"));
    }};

    public Collection<Photo> getAll() {
        return db.values();
    }

    public Photo get(String id) {
        return db.get(id);
    }

    public Photo remove(String id) {
        return db.remove(id);
    }

    public Photo save(String fileName, String contentType, byte[] data) {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setContentType(contentType);
        photo.setId(getLastId() + 1);
        photo.setData(data);
        db.put(String.valueOf(photo.getId()), photo);
        return photo;
    }

    private int getLastId(){
        return Integer.parseInt(new TreeMap<>(db).lastKey());
    }
}
