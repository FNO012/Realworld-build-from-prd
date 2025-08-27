package com.realworld.conduit.service;

import com.realworld.conduit.dto.ArticleRequest;
import com.realworld.conduit.dto.ArticleResponse;
import com.realworld.conduit.mapper.ArticleMapper;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.mapper.ArticleFavoriteMapper;
import com.realworld.conduit.mapper.ArticleTagMapper;
import com.realworld.conduit.mapper.TagMapper;
import com.realworld.conduit.mapper.UserFollowMapper;
import com.realworld.conduit.model.Article;
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
class ArticleServiceTest {

    @Mock
    private ArticleMapper articleMapper;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private ArticleFavoriteMapper articleFavoriteMapper;
    
    @Mock
    private ArticleTagMapper articleTagMapper;
    
    @Mock
    private TagMapper tagMapper;
    
    @Mock
    private UserFollowMapper userFollowMapper;
    
    @InjectMocks
    private ArticleService articleService;
    
    private User testUser;
    private Article testArticle;
    private ArticleRequest testRequest;

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
        testArticle.setDescription("Test Description");
        testArticle.setBody("Test Body");
        testArticle.setAuthorId(1L);
        testArticle.setCreatedAt(LocalDateTime.now());
        testArticle.setUpdatedAt(LocalDateTime.now());
        testArticle.setAuthorUsername("testuser");
        testArticle.setAuthorEmail("test@example.com");
        testArticle.setAuthorBio("Test Bio");

        testRequest = new ArticleRequest();
        testRequest.setTitle("Test Article");
        testRequest.setDescription("Test Description");
        testRequest.setBody("Test Body");
    }

    @Test
    @DisplayName("아티클 생성 성공 테스트")
    void createArticle_Success() {
        // given
        when(userMapper.findByEmail("test@example.com")).thenReturn(testUser);
        when(articleMapper.existsBySlug(anyString())).thenReturn(false);
        when(articleMapper.findBySlug(anyString())).thenReturn(testArticle);
        when(articleFavoriteMapper.countFavorites(any())).thenReturn(0);
        doNothing().when(articleMapper).insertArticle(any(Article.class));

        // when
        ArticleResponse response = articleService.createArticle(testRequest, "test@example.com");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Article");
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.getBody()).isEqualTo("Test Body");
        assertThat(response.getAuthor().getUsername()).isEqualTo("testuser");

        verify(userMapper).findByEmail("test@example.com");
        verify(articleMapper).insertArticle(any(Article.class));
        verify(articleMapper).findBySlug(anyString());
    }

    @Test
    @DisplayName("아티클 생성 실패 - 사용자 없음")
    void createArticle_UserNotFound() {
        // given
        when(userMapper.findByEmail("test@example.com")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> articleService.createArticle(testRequest, "test@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");

        verify(userMapper).findByEmail("test@example.com");
        verify(articleMapper, never()).insertArticle(any());
    }

    @Test
    @DisplayName("아티클 조회 성공 테스트")
    void getArticle_Success() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(articleFavoriteMapper.countFavorites(any())).thenReturn(5);

        // when
        ArticleResponse response = articleService.getArticle("test-article-slug");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSlug()).isEqualTo("test-article-slug");
        assertThat(response.getTitle()).isEqualTo("Test Article");

        verify(articleMapper).findBySlug("test-article-slug");
    }

    @Test
    @DisplayName("아티클 조회 실패 - 아티클 없음")
    void getArticle_NotFound() {
        // given
        when(articleMapper.findBySlug("non-existent-slug")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> articleService.getArticle("non-existent-slug"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아티클을 찾을 수 없습니다.");

        verify(articleMapper).findBySlug("non-existent-slug");
    }

    @Test
    @DisplayName("아티클 목록 조회 테스트")
    void getArticles_Success() {
        // given
        Article article1 = new Article();
        article1.setId(1L);
        article1.setSlug("article-1");
        article1.setTitle("Article 1");
        article1.setAuthorUsername("user1");

        Article article2 = new Article();
        article2.setId(2L);
        article2.setSlug("article-2");
        article2.setTitle("Article 2");
        article2.setAuthorUsername("user2");

        List<Article> articles = Arrays.asList(article1, article2);
        when(articleMapper.findAllArticles(0, 20)).thenReturn(articles);
        when(articleFavoriteMapper.countFavorites(any())).thenReturn(3);

        // when
        List<ArticleResponse> response = articleService.getArticles(0, 20);

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getTitle()).isEqualTo("Article 1");
        assertThat(response.get(1).getTitle()).isEqualTo("Article 2");

        verify(articleMapper).findAllArticles(0, 20);
    }

    @Test
    @DisplayName("아티클 수정 성공 테스트")
    void updateArticle_Success() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(userMapper.findByEmail("test@example.com")).thenReturn(testUser);
        when(articleFavoriteMapper.countFavorites(any())).thenReturn(2);
        doNothing().when(articleMapper).updateArticle(any(Article.class));

        ArticleRequest updateRequest = new ArticleRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setBody("Updated Body");

        Article updatedArticle = new Article();
        updatedArticle.setSlug("test-article-slug");
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setDescription("Updated Description");
        updatedArticle.setBody("Updated Body");
        updatedArticle.setAuthorId(1L);
        updatedArticle.setAuthorUsername("testuser");

        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle).thenReturn(updatedArticle);

        // when
        ArticleResponse response = articleService.updateArticle("test-article-slug", updateRequest, "test@example.com");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Updated Title");
        assertThat(response.getDescription()).isEqualTo("Updated Description");
        assertThat(response.getBody()).isEqualTo("Updated Body");

        verify(articleMapper, times(2)).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("test@example.com");
        verify(articleMapper).updateArticle(any(Article.class));
    }

    @Test
    @DisplayName("아티클 수정 실패 - 권한 없음")
    void updateArticle_Unauthorized() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        
        when(userMapper.findByEmail("other@example.com")).thenReturn(otherUser);

        // when & then
        assertThatThrownBy(() -> articleService.updateArticle("test-article-slug", testRequest, "other@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("수정 권한이 없습니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("other@example.com");
        verify(articleMapper, never()).updateArticle(any());
    }

    @Test
    @DisplayName("아티클 삭제 성공 테스트")
    void deleteArticle_Success() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        when(userMapper.findByEmail("test@example.com")).thenReturn(testUser);
        doNothing().when(articleMapper).deleteBySlug("test-article-slug");

        // when
        articleService.deleteArticle("test-article-slug", "test@example.com");

        // then
        verify(articleMapper).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("test@example.com");
        verify(articleMapper).deleteBySlug("test-article-slug");
    }

    @Test
    @DisplayName("아티클 삭제 실패 - 권한 없음")
    void deleteArticle_Unauthorized() {
        // given
        when(articleMapper.findBySlug("test-article-slug")).thenReturn(testArticle);
        
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        
        when(userMapper.findByEmail("other@example.com")).thenReturn(otherUser);

        // when & then
        assertThatThrownBy(() -> articleService.deleteArticle("test-article-slug", "other@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("삭제 권한이 없습니다.");

        verify(articleMapper).findBySlug("test-article-slug");
        verify(userMapper).findByEmail("other@example.com");
        verify(articleMapper, never()).deleteBySlug(anyString());
    }

    @Test
    @DisplayName("전체 아티클 수 조회 테스트")
    void getTotalArticlesCount_Success() {
        // given
        when(articleMapper.countAllArticles()).thenReturn(5);

        // when
        int count = articleService.getTotalArticlesCount();

        // then
        assertThat(count).isEqualTo(5);
        verify(articleMapper).countAllArticles();
    }
}