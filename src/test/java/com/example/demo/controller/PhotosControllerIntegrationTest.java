package com.example.demo.controller;

import com.example.demo.config.DataLoader;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PhotosControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhotosRepository photosRepository;

    @Autowired
    private DataLoader dataLoader;

    @Test
    @DisplayName("Get all photos should return list of photos")
    public void testGetAllPhotos() throws Exception {
        Photo photo1 = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo1);
        Photo photo2 = dataLoader.generatePhotos().get(1);
        photosRepository.save(photo2);

        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(photo1.getId()))
                .andExpect(jsonPath("$[0].fileName").value(photo1.getFileName()))
                .andExpect(jsonPath("$[0].contentType").value(photo1.getContentType()))
                .andExpect(jsonPath("$[0].category").value(photo1.getCategory().toString()))
                .andExpect(jsonPath("$[1].fileName").value(photo2.getFileName()));
    }

    @Test
    @DisplayName("Get photo by id should return photo")
    public void testGetPhotoById() throws Exception {
        Photo photo = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo);

        mockMvc.perform(get("/photos/{id}", photo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(photo.getId()))
                .andExpect(jsonPath("$.fileName").value(photo.getFileName()))
                .andExpect(jsonPath("$.contentType").value(photo.getContentType()))
                .andExpect(jsonPath("$.category").value(photo.getCategory().toString()));
    }

    @Test
    @DisplayName("Get photo by id should return 404 when photo does not exist")
    public void testGetPhotoByIdNotFound() throws Exception {
        // given
        int nonExistentFileId = 999;

        //then
        mockMvc.perform(get("/photos/{id}", nonExistentFileId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(String.format("404 NOT_FOUND \"Photo with id=%d not found\"", nonExistentFileId), result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Get photo by category should return list of photos")
    public void testGetPhotoByCategory() throws Exception {
        Photo photo1 = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo1);
        Photo photo2 = dataLoader.generatePhotos().get(1);
        photosRepository.save(photo2);
        String[] categories = new String[]{Category.WORK.toString()};

        mockMvc.perform(get("/photos/filter/category")
                .param("values", categories))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(photo2.getId()))
                .andExpect(jsonPath("$[0].fileName").value(photo2.getFileName()))
                .andExpect(jsonPath("$[0].contentType").value(photo2.getContentType()))
                .andExpect(jsonPath("$[0].category").value(photo2.getCategory().toString()));
    }

    @Test
    @DisplayName("Get photo by category should return 404 when no photos match the category")
    public void testGetPhotoByCategoryNotFound() throws Exception {
        Photo photo = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo);
        String[] categories = new String[]{Category.WORK.toString()};

        mockMvc.perform(get("/photos/filter/category")
                .param("values", categories))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Photos with such categories not found\"", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Delete photo should return 204")
    public void testDeletePhoto() throws Exception {
        Photo photo = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo);

        mockMvc.perform(delete("/photos/{id}", photo.getId()))
                .andExpect(status().isNoContent());
        assertTrue(photosRepository.findById(photo.getId()).isEmpty());
    }

    @Test
    @DisplayName("Create photo should return 200")
    public void testCreatePhoto() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "test.jpg", "image/jpg", new byte[]{1,2,3});

        mockMvc.perform(multipart("/photos")
                .file(file)
                .param("category", Category.FRIENDS.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fileName").value("test.jpg"))
                .andExpect(jsonPath("$.category").value(Category.FRIENDS.toString()));
    }

    @Test
    @DisplayName("Create photo should return 400 when uploaded file is not an image")
    public void testCreatePhotoWithInvalidContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "test.jpg", "text/plain", new byte[]{1,2,4});

        mockMvc.perform(multipart("/photos")
                .file(file)
                .param("category", Category.DOG.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("File " + file.getOriginalFilename() + " is not an image", result.getResolvedException().getMessage()));

        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}