package com.its.blogapi.controller;

import com.its.blogapi.dto.UserDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.User;
import com.its.blogapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.saveUser(userDto), CREATED);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto userDto) {
        return new ResponseEntity<User>((MultiValueMap<String, String>) userService.updateUser(userDto, userId), OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Deleted Successfully", OK);
    }
}
