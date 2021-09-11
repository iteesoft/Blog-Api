package com.its.blogapi.service.impl;

import com.its.blogapi.dto.CommentDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Post;
import com.its.blogapi.model.User;
import com.its.blogapi.repository.CommentRepository;
import com.its.blogapi.repository.PostRepository;
import com.its.blogapi.repository.UserRepository;
import com.its.blogapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Override
    public Comment saveComment(LoginDto loginDto, Long post_id, CommentDto commentDto) {
        Comment newComment = new Comment();
        Post post = postRepository.findPostById(post_id);
        User user = userRepository.findUserByEmail(loginDto.getEmail());

        newComment.setContent(commentDto.getContent());
        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setLikesQty(0);
        newComment.setCreatedTime(Instant.now());
        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(long id, Comment commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found", "Comment", "id", id));

        comment.setContent(commentRequest.getContent());
        comment.setUpdatedTime(Instant.now());
        return commentRepository.save(comment);
    }
    @Override
    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found", "Comment", "id", id));
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getUserComments(LoginDto loginDto) {
        User user = userRepository.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        return user.getComments();
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
        User user = userRepository.findUserByEmail(email);

        List<Comment> commentsLiked = user.getCommentsLiked();
        if (!commentsLiked.contains(comment)) {
            responseBody.setMessage("Comment successfully liked!");
            comment.setLikesQty(comment.getLikesQty()+1);
            commentsLiked.add(comment);
        } else {
            comment.setLikesQty(comment.getLikesQty()-1);
            commentsLiked.remove(comment);
            responseBody.setMessage("Comment unliked!");
        }
        responseBody.setData(comment);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
