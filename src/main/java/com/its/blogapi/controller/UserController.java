package com.its.blogapi.controller;

import com.its.blogapi.dto.PostDto;
import com.its.blogapi.dto.UserDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Friend;
import com.its.blogapi.model.Post;
import com.its.blogapi.service.PostService;
import com.its.blogapi.service.UserService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;
    private PostService postService;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginUser) {
        return new ResponseEntity<>(userService.login(loginUser), OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto newUser) {
        return new ResponseEntity<>(userService.createUser(newUser), CREATED);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> showUserDetails(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto currentUser) {
        return new ResponseEntity<>((userService.updateUser(currentUser, userId)), OK);
    }

    @SneakyThrows
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User Deleted Successfully", OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAccountDelete(@PathVariable("id") Long id) {
        userService.cancelProfileDelete(id);
        return new ResponseEntity<>("Delete Request Cancelled successfully", OK);
    }

    @PostMapping("/newFriend/{friend_id}")
    public ResponseEntity<?> addFriend(@PathVariable("friend_id") Long id, @RequestBody LoginDto currentUser) {
        userService.saveFriend(currentUser, id);
        return ResponseEntity.ok("Friend added successfully");
    }

    @GetMapping("/friends")
    public ResponseEntity<?> showFriends(@RequestBody LoginDto currentUser) {
        Set<Friend> myFriends = userService.getAllFriends(currentUser);
        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }

    @DeleteMapping("/{friend_id}")
    public void removeFriend(LoginDto loginDto, @PathVariable("friend_id") Long id) {
        userService.deleteFriend(loginDto, id);
    }

    @GetMapping("/comments")
    public ResponseEntity<?> showComments(@RequestBody LoginDto currentUser) {
        List<Comment> userComments = userService.getUserComments(currentUser);
        return ResponseEntity.ok(userComments);
    }
    @GetMapping("/posts")
    public List<PostDto> showAllPosts() {
        return postService.getAllPosts().stream().map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/myPosts")
    public ResponseEntity<?> showMyPosts(@RequestBody LoginDto currentUser) {
        List<Post> userPosts = userService.getUserPosts(currentUser);
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/likedComments")
    public ResponseEntity<?> showLikedComments(@RequestBody LoginDto currentUser) {
        List<Comment> userLikedComments = userService.getUserLikedComments(currentUser);
        return ResponseEntity.ok(userLikedComments);
    }

    @GetMapping("/likedPosts")
    public ResponseEntity<?> showLikedPosts(@RequestBody LoginDto currentUser) {
        List<Post> userLikedPosts = userService.getUserLikedPosts(currentUser);
        return ResponseEntity.ok(userLikedPosts);
    }

    @GetMapping("/friendPosts")
    public ResponseEntity<?> showFriendsPosts(@RequestBody LoginDto currenUser) {
        List<List<Post>> friendsPosts = userService.getFriendsPosts(currenUser);
        return ResponseEntity.ok(friendsPosts);
    }
}
