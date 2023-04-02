package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Photo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotosRepository extends CrudRepository<Photo, Integer> {

    @Query("""
        SELECT * FROM PHOTOS
        WHERE category in (:categories)
        """)
    public List<Photo> getByCategory(@Param("categories") List<Category> categoryList);
}
