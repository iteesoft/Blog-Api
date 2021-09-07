package com.its.blogapi.service;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();
    Post savePost(LoginDto loginDto, String content, String title);

    Post updatePost(long id, Post post);

    void deletePost(long id);

    Post getPostById(long id);
    ResponseEntity<?> likePost(Long id, Long user_id);
}
