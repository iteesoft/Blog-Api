package com.its.blogapi.service;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Comment;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CommentService {
    Comment saveComment(LoginDto loginDto, Long post_id, CommentDto commentDto);
    Comment updateComment(long id, CommentDto commentRequest);
    void deleteComment(long id);
    List<Comment> getUserComments(LoginDto loginDto);
    Comment getCommentById(long id);
    ResponseEntity<?> likeComment(Long id, String email);

}
