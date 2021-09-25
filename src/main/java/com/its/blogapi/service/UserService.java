package com.its.blogapi.service;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.UserDto;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Friend;
import com.its.blogapi.model.Post;
import com.its.blogapi.model.BlogUser;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

//    List<User> getAllUsers();
    BlogUser getUserById(Long id);
    ResponseEntity<BlogUser> updateUser(UserDto userDto, Long id);
    ResponseEntity<Map<String, Boolean>> deleteUser(Long id);
    void cancelProfileDelete(Long id);
    ResponseEntity<?> deleteUserById(Long id) throws InterruptedException;

    ResponseEntity<?> createUser(UserDto userDto);
    ResponseEntity<?> login(LoginDto loginDto);

    BlogUser getUserByEmail(String email);
    Set<Friend> getUserFriends(LoginDto loginDto);
    List<Comment> getUserComments(LoginDto loginDto);

    List<Post> getUserPosts(LoginDto currentUser);

    List<Comment> getUserLikedComments(LoginDto loginDto);

    List<Post> getUserLikedPosts(LoginDto currentUser);
    List<List<Post>> getFriendsPosts(LoginDto currentUser);
    ResponseEntity<?> saveFriend(LoginDto loginDto, Long id);
    Set<Friend> getAllFriends(LoginDto loginDto);
    void deleteFriend(LoginDto loginDto, Long friend_id);

}
