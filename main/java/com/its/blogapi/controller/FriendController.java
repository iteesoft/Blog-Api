package com.its.blogapi.controller;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Friend;
import com.its.blogapi.service.FriendService;
import com.its.blogapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
public class FriendController {

    private FriendService friendService;

    @GetMapping("addFriend")
    public ResponseEntity<?> addFriend(@RequestParam("friend_id") Long id, LoginDto currentUser) {
        friendService.saveFriend(currentUser, id);
        return ResponseEntity.ok("Friend added successfully");
    }

    @GetMapping("showUserFriends")
    public ResponseEntity<List<Friend>> getFriends(LoginDto loginDto) {
        List<Friend> myFriends = friendService.getUserFriends(loginDto);
        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }
}
