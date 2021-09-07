package com.its.blogapi.service.impl;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.dto.ResponseBody;
import com.its.blogapi.dto.UserDto;
import com.its.blogapi.exception.ResourceNotFoundException;
import com.its.blogapi.model.User;
import com.its.blogapi.repository.UserRepository;
import com.its.blogapi.service.UserService;
import com.its.blogapi.exception.AppExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> saveUser(UserDto userDto) throws AppExceptions{
        User newUser = new User();
        ResponseBody responseBody = new ResponseBody();

        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        newUser.setCreatedDate(Instant.now(Clock.systemDefaultZone()));

        Optional<User> tempUser = userRepository.getUserByEmailAndPassword(newUser.getEmail(), newUser.getPassword());
        if (tempUser.isEmpty()) {
            userRepository.save(newUser);
            responseBody.setMessage("Registration successful");
            responseBody.setData(newUser);
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        }
        responseBody.setMessage("Email is already in use");
        throw new AppExceptions("Sorry, The Provided Email Is Already In Use", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseBody> login(LoginDto loginDto) throws AppExceptions {
        Optional<User> loginUser = userRepository.getUserByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (loginUser.isPresent()) {
            ResponseBody<User> responseBody = new ResponseBody<>();
            responseBody.setMessage("Login Successful");
            responseBody.setData(loginUser.get());
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        throw new AppExceptions("Password or Email is not correct", HttpStatus.BAD_REQUEST);
    }

    @Override
    public User getUserById(Long id) throws ResourceNotFoundException {
//        Optional<User> user = userRepository.findById(id);
//        if (user.isPresent()) {
//            return user.get();
//        } else {
//            throw new ResourceNotFoundException("User Not Found", "User", "Id", id);
//        }

        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found","User", "id", id));
    }

    @Override
    public ResponseEntity<User> updateUser(UserDto userDto, Long id) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found","User", "id", id));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User Not Found", "User", "id", id));
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("User deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
