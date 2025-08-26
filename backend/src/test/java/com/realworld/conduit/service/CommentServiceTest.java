package com.realworld.conduit.service;

import com.realworld.conduit.dto.CommentRequest;
import com.realworld.conduit.dto.CommentResponse;
import com.realworld.conduit.mapper.ArticleMapper;
import com.realworld.conduit.mapper.CommentMapper;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.Article;
import com.realworld.conduit.model.Comment;
import com.realworld.conduit.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentMapper commentMapper;
    
    @Mock
    private ArticleMapper articleMapper;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private CommentService commentService;
    
    private User testUser;
    private Article testArticle;
    private Comment testComment;
    private CommentRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        testArticle = new Article();
        testArticle.setId(1L);
        testArticle.setSlug("test-article-slug");
        testArticle.setTitle("Test Article");
        testArticle.setAuthorId(1L);

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setBody("Test Comment");
        testComment.setArticleId(1L);
        testComment.setAuthorId(1L);
        testComment.setCreatedAt(LocalDateTime.now());
        testComment.setAuthorUsername("testuser");
        testComment.setAuthorEmail("test@example.com");
        testComment.setAuthorBio("Test Bio");

        testRequest = new CommentRequest();
        testRequest.setBody("Test Comment");
    }

    @Test
    @DisplayName("댓글 생성 성공 테스트")
    void createComment_Success() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(userMapper.findByEmail("test@example.com")).thenReturn(testUser);
        doAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            return null;
        }).when(commentMapper).insertComment(any(Comment.class));
        when(commentMapper.findById(1L)).thenReturn(testComment);

        // when
        CommentResponse response = commentService.createComment("test-article-slug", testRequest, "test@example.com");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo("Test Comment");
        assertThat(response.getAuthor().getUsername()).isEqualTo("testuser");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("test@example.com");
        verify(commentMapper).insertComment(any(Comment.class));
        verify(commentMapper).findById(anyLong());
    }

    @Test
    @DisplayName("댓글 생성 실패 - 아티클 없음")
    void createComment_ArticleNotFound() {
        // given
        when(articleMapper.findBySlug("non-existent-slug")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> commentService.createComment("non-existent-slug", testRequest, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("non-existent-slug");
        verify(userMapper, never()).findByEmail(anyString());
        verify(commentMapper, never()).insertComment(any());
    }

    @Test
    @DisplayName("댓글 생성 실패 - 사용자 없음")
    void createComment_UserNotFound() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(userMapper.findByEmail("test@example.com")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> commentService.createComment("test-article-slug", testRequest, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("test@example.com");
        verify(commentMapper, never()).insertComment(any());
    }

    @Test
    @DisplayName("댓글 목록 조회 성공 테스트")
    void getComments_Success() {
        // given
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setBody("First Comment");
        comment1.setAuthorUsername("user1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setBody("Second Comment");
        comment2.setAuthorUsername("user2");

        List<Comment> comments = Arrays.asList(comment1, comment2);
        
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(commentMapper.findByArticleSlug("test-article-slug")).thenReturn(comments);

        // when
        List<CommentResponse> response = commentService.getComments("test-article-slug");

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getBody()).isEqualTo("First Comment");
        assertThat(response.get(0).getAuthor().getUsername()).isEqualTo("user1");
        assertThat(response.get(1).getBody()).isEqualTo("Second Comment");
        assertThat(response.get(1).getAuthor().getUsername()).isEqualTo("user2");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(commentMapper).findByArticleSlug("test-article-slug");
    }

    @Test
    @DisplayName("댓글 목록 조회 실패 - 아티클 없음")
    void getComments_ArticleNotFound() {
        // given
        when(articleMapper.findBySlug("non-existent-slug")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> commentService.getComments("non-existent-slug"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("non-existent-slug");
        verify(commentMapper, never()).findByArticleSlug(anyString());
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteComment_Success() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(commentMapper.findById(1L)).thenReturn(testComment);
        when(userMapper.findByEmail("test@example.com")).thenReturn(testUser);
        doNothing().when(commentMapper).deleteById(1L);

        // when
        commentService.deleteComment("test-article-slug", 1L, "test@example.com");

        // then
        verify(articleMapper).findBySlug("test-article-slug");
        verify(commentMapper).findById(1L);
        verify(userMapper).findByEmail("test@example.com");
        verify(commentMapper).deleteById(1L);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 아티클 없음")
    void deleteComment_ArticleNotFound() {
        // given
        when(articleMapper.findBySlug("non-existent-slug")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment("non-existent-slug", 1L, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("non-existent-slug");
        verify(commentMapper, never()).findById(anyLong());
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 없음")
    void deleteComment_CommentNotFound() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(commentMapper.findById(999L)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment("test-article-slug", 999L, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(commentMapper).findById(999L);
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 없음")
    void deleteComment_Unauthorized() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(commentMapper.findById(1L)).thenReturn(testComment);
        
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        
        when(userMapper.findByEmail("other@example.com")).thenReturn(otherUser);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment("test-article-slug", 1L, "other@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("삭제 권한이 없습니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(commentMapper).findById(1L);
        verify(userMapper).findByEmail("other@example.com");
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 다른 아티클의 댓글")
    void deleteComment_WrongArticle() {
        // given
        Comment otherArticleComment = new Comment();
        otherArticleComment.setId(1L);
        otherArticleComment.setArticleId(2L); // 다른 아티클의 댓글
        otherArticleComment.setAuthorId(1L);
        
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(commentMapper.findById(1L)).thenReturn(otherArticleComment);

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment("test-article-slug", 1L, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("해당 아티클의 댓글이 아닙니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(commentMapper).findById(1L);
        verify(commentMapper, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("댓글 수 조회 테스트")
    void getCommentsCount_Success() {
        // given
        when(commentMapper.countByArticleSlug("test-article-slug")).thenReturn(3);

        // when
        int count = commentService.getCommentsCount("test-article-slug");

        // then
        assertThat(count).isEqualTo(3);
        verify(commentMapper).countByArticleSlug("test-article-slug");
    }
}