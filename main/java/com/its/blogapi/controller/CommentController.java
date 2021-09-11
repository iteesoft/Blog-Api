package com.its.blogapi.controller;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.PostDto;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Post;
import com.its.blogapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private CommentService commentService;

    @PostMapping("/add/{id}")
    public ResponseEntity<Comment> addCommentToPost(@RequestBody CommentDto commentDto, LoginDto loginDto, @PathVariable("id") Long post_id){
        Comment newComment = commentService.saveComment(loginDto, post_id, commentDto);
        return new ResponseEntity(newComment, HttpStatus.CREATED);
    }
}
