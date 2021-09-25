package com.its.blogapi.service.impl;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.dto.UserDto;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.BlogUser;
import com.its.blogapi.model.Comment;
import com.its.blogapi.model.Friend;
import com.its.blogapi.model.Post;
import com.its.blogapi.repository.BlogUserRepository;
import com.its.blogapi.service.UserService;
import com.its.blogapi.exception.AppExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final BlogUserRepository blogUserRepository;

    @Autowired
    public UserServiceImpl(BlogUserRepository blogUserRepository) {
        this.blogUserRepository = blogUserRepository;
    }

    @Override
    public ResponseEntity<?> createUser(UserDto userDto) throws AppExceptions{
        BlogUser newBlogUser = new BlogUser();
        ResponseBody responseBody = new ResponseBody();

        newBlogUser.setFirstName(userDto.getFirstName());
        newBlogUser.setLastName(userDto.getLastName());
        newBlogUser.setEmail(userDto.getEmail());
        newBlogUser.setPassword(userDto.getPassword());

        Optional<BlogUser> tempUser = blogUserRepository.getUserByEmailAndPassword(newBlogUser.getEmail(), newBlogUser.getPassword());
        if (tempUser.isEmpty()) {
            blogUserRepository.save(newBlogUser);
            responseBody.setMessage("Registration successful");
            responseBody.setData(newBlogUser);
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        }
        throw new AppExceptions("User with email: " + userDto.getEmail() + " already exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseBody> login(LoginDto loginDto) throws AppExceptions {
        Optional<BlogUser> loginUser = blogUserRepository.getUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (loginUser.isPresent()) {
            ResponseBody<BlogUser> responseBody = new ResponseBody<>();
            responseBody.setMessage("Login Successful");
            responseBody.setData(loginUser.get());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        throw new AppExceptions("Password or Email incorrect", HttpStatus.BAD_REQUEST);
    }

    @Override
    public BlogUser getUserByEmail(String email) {
        return blogUserRepository.findByEmail(email);
    }

    @Override
    public BlogUser getUserById(Long id) throws ResourceNotFoundException {
//        Optional<User> user = userRepository.findById(id);
//        if (user.isPresent()) {
//            return user.get();
//        } else {
//            throw new ResourceNotFoundException("User Not Found", "User", "Id", id);
//        }

        return blogUserRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found","User", "id", id));
    }

    @Override
    public ResponseEntity<BlogUser> updateUser(UserDto userDto, Long id) throws ResourceNotFoundException{
        BlogUser existingBlogUser = blogUserRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found","User", "id", id));

        existingBlogUser.setFirstName(userDto.getFirstName());
        existingBlogUser.setLastName(userDto.getLastName());
        existingBlogUser.setEmail(userDto.getEmail());
        existingBlogUser.setPassword(userDto.getPassword());

        BlogUser updatedBlogUser = blogUserRepository.save(existingBlogUser);
        return ResponseEntity.ok(updatedBlogUser);
    }

//    public void delayDeleteUser() {
//        this.deleteUser(id);
//    }

    @Override
//    @Scheduled(initialDelay = 15000)
    public ResponseEntity<Map<String, Boolean>> deleteUser(Long id) {
        BlogUser blogUser = blogUserRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User Not Found", "User", "id", id));
        Map<String, Boolean> response = new HashMap<>();

        if (blogUser.getEnableDelete()) {
            blogUserRepository.delete(blogUser);
            response.put("User deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        }
        response.put("User have cancelled the delete request", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> deleteUserById(Long id) throws InterruptedException {
        ResponseBody response = new ResponseBody();

        BlogUser user = blogUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found", "User", "id", id));
        Thread.sleep(10000);
        if (user.getEnableDelete()) {
            blogUserRepository.delete(user);
            response.setMessage("User deleted");
        }
        response.setMessage("User have cancelled the delete request");
        return ResponseEntity.ok(response);
    }

    @Override
    public void cancelProfileDelete(Long id) {
        BlogUser blogUser = blogUserRepository.findById(id).orElseThrow(() ->
                new AppExceptions("User with id " + id + "does not exist", HttpStatus.BAD_REQUEST));
        if (blogUser.getEnableDelete()){
            blogUser.setEnableDelete(false);
        }
    }

    @Override
    public List<Comment> getUserComments(LoginDto loginDto) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        return loginBlogUser.getComments();
    }

    @Override
    public List<Post> getUserPosts(LoginDto currentUser) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(currentUser.getEmail());
        return loginBlogUser.getPosts();
    }

    @Override
    public Set<Friend> getUserFriends(LoginDto loginDto) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        return loginBlogUser.getConnections();
    }

    @Override
    public List<Comment> getUserLikedComments(LoginDto loginDto) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        return loginBlogUser.getCommentsLiked();
    }

    @Override
    public List<Post> getUserLikedPosts(LoginDto currentUser) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(currentUser.getEmail());
        return loginBlogUser.getPostsLiked();
    }
    @Override
    public List<List<Post>> getFriendsPosts(LoginDto currentUser) {
        BlogUser loginBlogUser = blogUserRepository.findUserByEmail(currentUser.getEmail());
        Set<Friend> userFriends = loginBlogUser.getConnections();
        List<List<Post>> friendsPost = new ArrayList<>();

        for (Friend friend : userFriends) {

//            friendsPost.add(friend.getPosts());
        }
        return friendsPost;
    }
    @Override
    public ResponseEntity<?> saveFriend(LoginDto loginDto, Long id) {
        ResponseBody<BlogUser> responseBody = new ResponseBody<>();
        BlogUser blogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        BlogUser friend = blogUserRepository.findUserById(id);

        if (blogUser.getConnections().contains(friend)) {
            responseBody.setMessage(friend.getFirstName() + " is already your friend");
        } else {
            Set<Friend> friends = blogUser.getConnections();
            friends.add(friend);
            blogUser.setConnections(friends);
//            friend.getUserConnections().add(user);
            blogUserRepository.save(blogUser);
        }
        return ResponseEntity.ok(responseBody);
    }

    @Override
    public Set<Friend> getAllFriends(LoginDto loginDto) {
        BlogUser blogUser = blogUserRepository.findUserByEmail(loginDto.getEmail());
        return blogUser.getConnections();
    }

    @Override
    public void deleteFriend(LoginDto loginDto, Long friend_id) {
        BlogUser blogUser = blogUserRepository.findUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        BlogUser friend = blogUserRepository.findUserById(friend_id);
        if (blogUser.getConnections().contains(friend)) {
            blogUser.getConnections().remove(friend);
        } else {
            throw new AppExceptions("User does not exist in your friend list", HttpStatus.BAD_REQUEST);
        }
    }
//    @Override
//    public void deletePost(long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User Not Found", "User", "id", id));
//
//        userRepository.delete(user);
//    }
}
