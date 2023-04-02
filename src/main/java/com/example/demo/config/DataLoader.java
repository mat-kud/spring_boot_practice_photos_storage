package com.example.demo.config;

import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader{
    public List<Photo> generatePhotos() throws IOException {
        File fi = new File("src/test/resources/images/pic.jpg");
        byte[] fileContent = Files.readAllBytes(fi.toPath());
        Photo photo1 = new Photo();
        photo1.setFileName("name1");
        photo1.setContentType("image/jpeg");
        photo1.setCategory(Category.HOBBY);
        photo1.setData(fileContent);
        photo1.setDateCreated(LocalDateTime.now());

        Photo photo2 = new Photo();
        photo2.setFileName("name2");
        photo2.setContentType("image/jpeg");
        photo2.setCategory(Category.WORK);
        photo2.setData(fileContent);
        photo2.setDateCreated(LocalDateTime.now());

        Photo photo3 = new Photo();
        photo3.setFileName("name3");
        photo3.setContentType("image/jpeg");
        photo3.setCategory(Category.VACATION);
        photo3.setData(fileContent);
        photo3.setDateCreated(LocalDateTime.now());

        Photo photo4 = new Photo();
        photo4.setFileName("name4");
        photo4.setContentType("image/jpeg");
        photo4.setCategory(Category.DOG);
        photo4.setData(fileContent);
        photo4.setDateCreated(LocalDateTime.now());

        return List.of(photo1, photo2, photo3, photo4);
    }

}
