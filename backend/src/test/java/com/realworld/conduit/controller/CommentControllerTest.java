package com.realworld.conduit.controller;

import com.realworld.conduit.dto.CommentRequest;
import com.realworld.conduit.dto.CommentResponse;
import com.realworld.conduit.dto.CommentResponse.AuthorResponse;
import com.realworld.conduit.service.CommentService;
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
class CommentControllerTest {

    @Mock
    private CommentService commentService;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private CommentController commentController;

    private CommentRequest validRequest;
    private CommentResponse sampleResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CommentRequest();
        validRequest.setBody("Test Comment");

        AuthorResponse author = new AuthorResponse();
        author.setUsername("testuser");
        author.setBio("Test bio");
        author.setImage(null);

        sampleResponse = new CommentResponse();
        sampleResponse.setId(1L);
        sampleResponse.setBody("Test Comment");
        sampleResponse.setAuthor(author);
        sampleResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("댓글 생성 API 테스트 - 성공")
    void createComment_Success() {
        // given
        when(authentication.getName()).thenReturn("test@example.com");
        when(commentService.createComment(eq("test-article"), any(CommentRequest.class), eq("test@example.com")))
                .thenReturn(sampleResponse);

        // when
        ResponseEntity<?> response = commentController.createComment("test-article", validRequest, authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(commentService).createComment(eq("test-article"), any(CommentRequest.class), eq("test@example.com"));
    }

    @Test
    @DisplayName("댓글 목록 조회 API 테스트 - 성공")
    void getComments_Success() {
        // given
        List<CommentResponse> comments = Arrays.asList(sampleResponse);
        when(commentService.getComments("test-article")).thenReturn(comments);
        when(commentService.getCommentsCount("test-article")).thenReturn(1);

        // when
        ResponseEntity<?> response = commentController.getComments("test-article");

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commentService).getComments("test-article");
        verify(commentService).getCommentsCount("test-article");
    }

    @Test
    @DisplayName("댓글 삭제 API 테스트 - 성공")
    void deleteComment_Success() {
        // given
        when(authentication.getName()).thenReturn("test@example.com");
        doNothing().when(commentService).deleteComment("test-article", 1L, "test@example.com");

        // when
        ResponseEntity<?> response = commentController.deleteComment("test-article", 1L, authentication);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(commentService).deleteComment("test-article", 1L, "test@example.com");
    }
}