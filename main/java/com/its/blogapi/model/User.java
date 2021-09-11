package com.its.blogapi.model;

import com.zaxxer.hikari.util.FastList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USERS")
public class User extends Friend{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY)
    private List<Post> postsLiked;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "user")
    private List<Comment> commentsLiked;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY)
    private List<Friend> friendList;
}
