package com.example.demo.service;

import com.example.demo.config.DataLoader;
import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhotosServiceTest {

    @Mock
    private PhotosRepository photosRepository;
    private PhotosService underTest;
    private final DataLoader dataLoader = new DataLoader();

    @BeforeEach
    void setUp() {
        underTest = new PhotosService(photosRepository);
    }

// Checking if findAll() was called when calling getAllPhotos() of the PhotoService
    @Test
    void canGetAllPhotos() {
        //when
        underTest.getAllPhotos();
        //then
        verify(photosRepository).findAll();
    }

    @Test
    void canGetById() {
        //given
        int id = 1;
        //when
        underTest.getById(id);
        //then
        ArgumentCaptor<Integer> idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(photosRepository).findById(idArgumentCaptor.capture());
        int capturedId = idArgumentCaptor.getValue();

        assertEquals(id, capturedId);
    }

    @Test
    void getByCategory() {
        //given
        List<Category> categories = List.of(Category.WORK, Category.HOBBY);
        //when
        underTest.getByCategory(categories);
        //then
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Category>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(photosRepository).getByCategory(listArgumentCaptor.capture());
        List<Category> capturedCategories = listArgumentCaptor.getValue();

        assertEquals(categories, capturedCategories);
    }

    @Test
    void canDeleteById() {
        //given
        int id = 1;
        //when
        underTest.remove(id);
        //then
        ArgumentCaptor<Integer> idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(photosRepository).deleteById(idArgumentCaptor.capture());
        int capturedId = idArgumentCaptor.getValue();

        assertEquals(id, capturedId);
    }

//Checking if object Photo passed as an argument to the save() method of the PhotoService is the same
//as the object passed to save() method of the PhotoRepository
    @Test
    void canAddPhoto() throws IOException {
        //given
        Photo photo = dataLoader.generatePhotos().get(0);
        //when
        underTest.save(photo.getFileName(), photo.getContentType(), photo.getCategory().toString(),
                photo.getData(), photo.getDateCreated());
        //then
        ArgumentCaptor<Photo> photoArgumentCaptor = ArgumentCaptor.forClass(Photo.class);
        verify(photosRepository).save(photoArgumentCaptor.capture());
        Photo capturedPhoto = photoArgumentCaptor.getValue();

        assertEquals(capturedPhoto, photo);
    }
}