package com.realworld.conduit.controller;

import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.dto.ArticleResponse.AuthorResponse;
import com.realworld.conduit.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private ArticleController articleController;

    private ArticleRequest validRequest;
    private ArticleResponse sampleResponse;

    @BeforeEach
    void setUp() {
        validRequest = new ArticleRequest();
        validRequest.setTitle("Test Article");
        validRequest.setDescription("Test Description");
        validRequest.setBody("Test Body");

        AuthorResponse author = new AuthorResponse();
        author.setUsername("testuser");
        author.setBio("Test bio");
        author.setImage(null);

        sampleResponse = new ArticleResponse();
        sampleResponse.setSlug("test-article");
        sampleResponse.setTitle("Test Article");
        sampleResponse.setDescription("Test Description");
        sampleResponse.setBody("Test Body");
        sampleResponse.setAuthor(author);
        sampleResponse.setCreatedAt(LocalDateTime.now());
        sampleResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("아티클 생성 API 테스트 - 성공")
    void createArticle_Success() {
        // given
        when(authentication.getName()).thenReturn("test@example.com");
        when(articleService.createArticle(any(ArticleRequest.class), eq("test@example.com")))
                .thenReturn(sampleResponse);

        // when
        ResponseEntity<?> response = articleController.createArticle(validRequest, authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(articleService).createArticle(any(ArticleRequest.class), eq("test@example.com"));
    }

    @Test
    @DisplayName("아티클 조회 API 테스트 - 성공")
    void getArticle_Success() {
        // given
        when(articleService.getArticle("test-article")).thenReturn(sampleResponse);

        // when
        ResponseEntity<?> response = articleController.getArticle("test-article", authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(articleService).getArticle("test-article");
    }

    @Test
    @DisplayName("아티클 목록 조회 API 테스트 - 성공")
    void getArticles_Success() {
        // given
        List<ArticleResponse> articles = Arrays.asList(sampleResponse);
        when(articleService.getArticles(0, 20)).thenReturn(articles);
        when(articleService.getTotalArticlesCount()).thenReturn(1);

        // when
        ResponseEntity<?> response = articleController.getArticles(0, 20, null, null, authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(articleService).getArticles(0, 20);
        verify(articleService).getTotalArticlesCount();
    }

    @Test
    @DisplayName("아티클 수정 API 테스트 - 성공")
    void updateArticle_Success() {
        // given
        when(authentication.getName()).thenReturn("test@example.com");
        when(articleService.updateArticle(eq("test-article"), any(ArticleRequest.class), eq("test@example.com")))
                .thenReturn(sampleResponse);

        // when
        ResponseEntity<?> response = articleController.updateArticle("test-article", validRequest, authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(articleService).updateArticle(eq("test-article"), any(ArticleRequest.class), eq("test@example.com"));
    }

    @Test
    @DisplayName("아티클 삭제 API 테스트 - 성공")
    void deleteArticle_Success() {
        // given
        when(authentication.getName()).thenReturn("test@example.com");
        doNothing().when(articleService).deleteArticle("test-article", "test@example.com");

        // when
        ResponseEntity<?> response = articleController.deleteArticle("test-article", authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(articleService).deleteArticle("test-article", "test@example.com");
    }
}