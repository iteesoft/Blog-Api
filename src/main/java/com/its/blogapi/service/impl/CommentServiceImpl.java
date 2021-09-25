package com.its.blogapi.service.impl;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.BlogUser;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Post;
import com.its.blogapi.repository.CommentRepository;
import com.its.blogapi.repository.PostRepository;
import com.its.blogapi.repository.BlogUserRepository;
import com.its.blogapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final BlogUserRepository blogUserRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(PostRepository postRepository, BlogUserRepository blogUserRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.blogUserRepository = blogUserRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment saveComment(LoginDto loginDto, Long post_id, CommentDto commentDto) {
        Comment newComment = new Comment();
        Post post = postRepository.findPostById(post_id);
        BlogUser blogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());

        newComment.setContent(commentDto.getContent());
        newComment.setBlogUser(blogUser);
        newComment.setPost(post);
        newComment.setLikesQty(0);
        newComment.setCreatedTime(Instant.now());
        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(long id, CommentDto commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment Not Found", "Comment", "id", id));

        comment.setContent(commentRequest.getContent());
        comment.setUpdatedTime(Instant.now());
        return commentRepository.save(comment);
    }
    @Override
    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment Not Found", "Comment", "id", id));
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getUserComments(LoginDto loginDto) {
        BlogUser blogUser = blogUserRepository.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        return blogUser.getComments();
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Comment Not Found", "Comment", "id", id));
    }

    @Transactional
    @Override
    public ResponseEntity<?> likeComment(Long id, String email) {
        ResponseBody<Comment> responseBody = new ResponseBody<>();
        Comment comment = getCommentById(id);
        BlogUser blogUser = blogUserRepository.findUserByEmail(email);

        List<Comment> commentsLiked = blogUser.getCommentsLiked();

        if (!commentsLiked.contains(comment)) {
            responseBody.setMessage("Comment Liked!");
            comment.setLikesQty(comment.getLikesQty()+1);
            commentsLiked.add(comment);
        } else {
            comment.setLikesQty(comment.getLikesQty()-1);
            commentsLiked.remove(comment);
            responseBody.setMessage("Comment Unliked!");
        }
        responseBody.setData(comment);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
