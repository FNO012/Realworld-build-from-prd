package com.realworld.conduit.service;

import com.realworld.conduit.dto.CommentRequest;
import com.realworld.conduit.dto.CommentResponse;
import com.realworld.conduit.mapper.ArticleMapper;
import com.realworld.conduit.mapper.CommentMapper;
import com.realworld.conduit.mapper.UserMapper;
import com.realworld.conduit.model.Article;
import com.realworld.conduit.model.Comment;
import com.realworld.conduit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    
    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    
    @Autowired
    public CommentService(CommentMapper commentMapper, 
                         ArticleMapper articleMapper, 
                         UserMapper userMapper) {
        this.commentMapper = commentMapper;
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
    }
    
    public CommentResponse createComment(String articleSlug, CommentRequest request, String userEmail) {
        Article article = articleMapper.findBySlug(articleSlug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        
        Comment comment = new Comment(request.getBody(), article.getId(), user.getId());
        commentMapper.insertComment(comment);
        
        Comment createdComment = commentMapper.findById(comment.getId());
        return new CommentResponse(createdComment);
    }
    
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(String articleSlug) {
        Article article = articleMapper.findBySlug(articleSlug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        List<Comment> comments = commentMapper.findByArticleSlug(articleSlug);
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
    
    public void deleteComment(String articleSlug, Long commentId, String userEmail) {
        Article article = articleMapper.findBySlug(articleSlug);
        if (article == null) {
            throw new RuntimeException("아티클을 찾을 수 없습니다.");
        }
        
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }
        
        if (!comment.getArticleId().equals(article.getId())) {
            throw new RuntimeException("해당 아티클의 댓글이 아닙니다.");
        }
        
        User user = userMapper.findByEmail(userEmail);
        if (user == null || !user.getId().equals(comment.getAuthorId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        commentMapper.deleteById(commentId);
    }
    
    @Transactional(readOnly = true)
    public int getCommentsCount(String articleSlug) {
        return commentMapper.countByArticleSlug(articleSlug);
    }
}