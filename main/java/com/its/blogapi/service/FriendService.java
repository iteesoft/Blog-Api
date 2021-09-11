package com.its.blogapi.service;

import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.model.Friend;

import java.util.List;

public interface FriendService {
    void saveFriend(LoginDto loginDto, Long id);
    List<Friend> getUserFriends(LoginDto loginDto);
}
