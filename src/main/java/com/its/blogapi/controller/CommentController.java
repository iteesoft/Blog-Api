package com.its.blogapi.controller;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Comment;
import com.its.blogapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/new/{post_id}")
    public ResponseEntity<Comment> add(@RequestBody CommentDto newComment, LoginDto currentUser, @PathVariable("post_id") Long post_id){
        Comment comment = commentService.saveComment(currentUser, post_id, newComment);
        return new ResponseEntity(comment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {
        Comment updatedComment = commentService.updateComment(id, commentDto);
        return  new ResponseEntity(updatedComment, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> like(@PathVariable("id") Long id, @RequestBody LoginDto currentUser) {
        return ResponseEntity.ok(commentService.likeComment(id,currentUser.getEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id")Long id) {
        commentService.deleteComment(id);
    }
}
