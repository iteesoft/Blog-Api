package com.its.blogapi.repository;

import com.its.blogapi.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> {

    Optional<BlogUser> getUserByEmailAndPassword(String email, String password);

    BlogUser findUserByEmail(String email);

    BlogUser findUserById(Long id);

    BlogUser findUserByEmailAndPassword(String email, String password);

    BlogUser findByEmail(String email);

    //  List<User> findAllUserByFriendId(Long id);
}
