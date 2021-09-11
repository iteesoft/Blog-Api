package com.its.blogapi.controller;

import com.its.blogapi.dto.PostDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Post;
import com.its.blogapi.service.PostService;
import com.its.blogapi.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private ModelMapper modelMapper;


    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts().stream().map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/getUserPosts/{email}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable LoginDto loginDto) {
        List<Post> userPost = postService.getUserPosts(loginDto);
        return new ResponseEntity<>(userPost, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        PostDto postResponse = modelMapper.map(post, PostDto.class);
        return ResponseEntity.ok().body(postResponse);
    }

    @PostMapping("/add")
    public ResponseEntity<Post> addPost(@RequestBody PostDto postDto, LoginDto loginDto){
        Post newPost = postService.savePost(loginDto, postDto);
       return new ResponseEntity(newPost, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable long id, @RequestBody PostDto postDto) {
        Post postRequest = modelMapper.map(postDto, Post.class);
        Post post = postService.updatePost(id, postRequest);
        return new ResponseEntity(post, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/like/{id}/{user_id}")
    public ResponseEntity<?> likePost(@PathVariable Long id, @PathVariable Long user_id) {
        return postService.likePost(id,user_id);
    }


}
