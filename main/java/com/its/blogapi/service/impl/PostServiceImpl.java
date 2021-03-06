package com.its.blogapi.service.impl;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.PostDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Post;
import com.its.blogapi.model.User;
import com.its.blogapi.repository.CommentRepository;
import com.its.blogapi.repository.PostRepository;
import com.its.blogapi.repository.UserRepository;
import com.its.blogapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;


    @Override
    public Post savePost(LoginDto loginDto, PostDto postDto) {
        Post newPost = new Post();
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        newPost.setUser(user);
        newPost.setLikesQty(0);
        newPost.setContent(postDto.getContent());
        newPost.setTitle(postDto.getTitle());
        newPost.setCreatedDate(Instant.now());
        return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(long id, Post postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post Not Found", "Post", "id", id));

        post.setContent(postRequest.getContent());
        post.setUpdatedDate(Instant.now());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post Not Found", "Post", "id", id));

        postRepository.delete(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getUserPosts(LoginDto loginDto) {
        User user = userRepository.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        return user.getPosts();
    }


    @Override
    public Post getPostById(long id) {
        Optional<Post> result = postRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Post Not Found", "Post", "id", id);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> likePost(Long id, Long user_id) {
        ResponseBody<Post> responseBody = new ResponseBody<>();
        Post post = getPostById(id);
        User user = userRepository.getById(user_id);

            List<Post> postLiked = user.getPostsLiked();
            if (!postLiked.contains(post)) {
                responseBody.setMessage("Post successfully liked");
                post.setLikesQty(post.getLikesQty()+1);
                postLiked.add(post);
            } else {
                post.setLikesQty(post.getLikesQty()-1);
                postLiked.remove(post);
                responseBody.setMessage("Post unliked");
            }
            responseBody.setData(post);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }


}
