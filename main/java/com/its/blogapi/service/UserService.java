package com.its.blogapi.service;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.UserDto;
import com.its.blogapi.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

//    List<User> getAllUsers();
    User getUserById(Long id);
    ResponseEntity<User> updateUser(UserDto userDto, Long id);
    ResponseEntity<Map<String, Boolean>> deleteUser(Long id);
    ResponseEntity<?> saveUser(UserDto userDto);
    ResponseEntity<?> login(LoginDto loginDto);
    User getUserByEmailAndPassword(String email, String password);

    User getUserByEmail(String email);
//    void deletePost(long id);
}
