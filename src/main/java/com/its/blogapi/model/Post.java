package com.its.blogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="POSTS")
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Instant createdDate;
    private Instant updatedDate;
    private int likesQty;

    @JsonIgnore
    private BlogUser blogUser;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "post")
    private List<Comment> postComments;

}
