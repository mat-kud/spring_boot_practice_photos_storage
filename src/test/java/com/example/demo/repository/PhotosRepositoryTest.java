package com.example.demo.repository;

import com.example.demo.config.DataLoader;
import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class PhotosRepositoryIntegrationTest {

    @Autowired
    private PhotosRepository underTest;
    @Autowired
    private DataLoader dataLoader;

    @Test
    public void checkIfPhotosAreFilteredByCategory() throws Exception {
        //given
        dataLoader.generatePhotos().forEach(e -> underTest.save(e));
        List<Category> categories = List.of(Category.WORK, Category.VACATION);

        //when
        List<Photo> filteredPhotos = underTest.getByCategory(categories);

        //then
        assertTrue(filteredPhotos.stream()
                .map(Photo::getCategory)
                .allMatch(c -> c.equals(Category.WORK) || c.equals(Category.VACATION)));
    }

}