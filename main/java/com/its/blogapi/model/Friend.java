package com.its.blogapi.model;

import lombok.Data;
import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name="FRIENDS")
public class Friend {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Instant createdDate;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "second_user_id", referencedColumnName = "id")
//    User secondUser;
}
