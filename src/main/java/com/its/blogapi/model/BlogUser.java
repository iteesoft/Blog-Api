package com.its.blogapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;
import java.util.Set;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Blog_users")
public class BlogUser extends Friend {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean enableDelete = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY)
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "blogUser")
    private List<Post> postsLiked;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY)
    private List<Post> favoritePosts;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY)
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "blogUser")
    private List<Comment> commentsLiked;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "connection_id"))
    private Set<Friend> connections;

}
