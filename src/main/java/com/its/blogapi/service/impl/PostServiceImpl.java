package com.its.blogapi.service.impl;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.PostDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.BlogUser;
import com.its.blogapi.model.Post;
import com.its.blogapi.repository.PostRepository;
import com.its.blogapi.repository.BlogUserRepository;
import com.its.blogapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "post")
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BlogUserRepository blogUserRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, BlogUserRepository blogUserRepository) {
        this.postRepository = postRepository;
        this.blogUserRepository = blogUserRepository;
    }

    @Caching(evict = {@CacheEvict(value = "allPostCache", allEntries = true),
            @CacheEvict(value = "postCache", key = "#post.id")
    })
    @Override
    public Post createPost(LoginDto loginDto, PostDto postDto) {
        Post newPost = new Post();
        BlogUser blogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        newPost.setBlogUser(blogUser);
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

    @Cacheable(value = "allPostCache")
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getUserPosts(LoginDto loginDto) {
        BlogUser blogUser = blogUserRepository.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        return blogUser.getPosts();
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
    public ResponseEntity<?> likePost(Long id, String email) {
        ResponseBody<Post> responseBody = new ResponseBody<>();
        Post post = getPostById(id);
        BlogUser blogUser = blogUserRepository.findByEmail(email);

            List<Post> postLiked = blogUser.getPostsLiked();
            if (!postLiked.contains(post)) {
                responseBody.setMessage("Post Liked");
                post.setLikesQty(post.getLikesQty()+1);
                postLiked.add(post);
            } else {
                post.setLikesQty(post.getLikesQty()-1);
                postLiked.remove(post);
                responseBody.setMessage("Post Unliked");
            }
            responseBody.setData(post);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }


}
