package com.example.demo.controller;

import com.example.demo.config.DataLoader;
import com.example.demo.model.Photo;
import com.example.demo.repository.PhotosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class DownloadControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PhotosRepository photosRepository;
    @Autowired
    private DataLoader dataLoader;


    @Test
    @DisplayName("Download the file by id should return the file")
    public void testDownloadById() throws Exception {
        //given
        Photo photo = dataLoader.generatePhotos().get(0);
        photosRepository.save(photo);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/download/{id}",photo.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        byte[] responseBytes = mvcResult.getResponse().getContentAsByteArray();
        MediaType responseContentType = MediaType.parseMediaType(Objects.requireNonNull(mvcResult.getResponse().getContentType()));
        String responseFilename = Objects.requireNonNull(mvcResult.getResponse().getHeader("Content-Disposition"))
                .split("=")[1]
                .replace("\"", "");

        assertThat(responseBytes).isEqualTo(photo.getData());
        assertThat(responseContentType).isEqualTo(MediaType.valueOf(photo.getContentType()));
        assertThat(responseFilename).isEqualTo(photo.getFileName());
    }

    @Test
    @DisplayName("Download the file by id should return 404 when is file not found" )
    public void testDownloadByIdWhenFileNotFound() throws Exception {
        //given
        int nonExistentFileId = 999;

        //when
        //then
        mockMvc.perform(get("/download/{id}", nonExistentFileId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"File not found\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
