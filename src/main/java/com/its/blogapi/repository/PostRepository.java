package com.its.blogapi.repository;

import com.its.blogapi.model.Post;
import com.its.blogapi.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostByBlogUserOrderById(BlogUser blogUser);
    List<Post> findAll();

    Post findPostById(Long id);
}
