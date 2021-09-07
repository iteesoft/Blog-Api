package com.its.blogapi.repository;

import com.its.blogapi.model.Friend;
import com.its.blogapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmailAndPassword(String email, String password);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User findUserByEmailAndPassword(String email, String password);

  //  List<User> findAllUserByFriendId(Long id);
}
