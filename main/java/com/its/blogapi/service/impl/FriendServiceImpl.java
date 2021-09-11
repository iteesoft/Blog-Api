package com.its.blogapi.service.impl;

import com.its.blogapi.dto.FriendDto;
import com.its.blogapi.dto.LoginDto;
import com.its.blogapi.exception.AppExceptions;
import com.its.blogapi.model.Friend;
import com.its.blogapi.model.User;
import com.its.blogapi.repository.FriendRepository;
import com.its.blogapi.repository.PostRepository;
import com.its.blogapi.repository.UserRepository;
import com.its.blogapi.service.FriendService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {

    private FriendRepository friendRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;


    @Override
    public void saveFriend(LoginDto loginDto, Long id) throws NullPointerException {
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        User friend = userRepository.findUserById(id);
        List<Friend> userFriends = user.getFriendList();

        if (!userFriends.contains(friend)) {
            user.getFriendList().add(friend);
            userRepository.save(user);
        }
        throw new AppExceptions("User already exist as a friend", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Friend> getUserFriends(LoginDto loginDto) {
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        List<Friend> userFriends = user.getFriendList();
        return userFriends;
    }




//        Optional<User> user = userRepository.findById(id);
//        LoginDto loginDto2 = modelMapper.map(user, LoginDto.class);
//
//        Friend friend = new Friend();
//        User user1 = userRepository.findUserByEmail(loginDto1.getEmail());
//        User user2 = userRepository.findUserByEmail(loginDto2.getEmail());
//        User firstUser = user1;
//        User secondUser = user2;
//
//        if (user1.getId() > user2.getId()) {
//            firstUser = user2;
//            secondUser = user1;
//        }
//        if (!(friendRepository.existsByFirstUserAndSecondUser(firstUser,secondUser))) {
//            friend.setCreatedDate(new Date());
//            friend.setFirstUser(firstUser);
//            friend.setSecondUser(secondUser);
//            friendRepository.save(friend);
//        }
    }


//
//        User currentUser = userRepository.findUserById(id);
//        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
//        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
//        List<User> friendUsers = new ArrayList<>();
//
//        for (Friend friend: friendsByFirstUser) {
//            friendUsers.add(userRepository.findUserById(friend.getSecondUser().getId()));
//        }
//        for (Friend friend: friendsBySecondUser) {
//            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()));
//        }
////        return friendUsers;
//    }
//
//
//}
