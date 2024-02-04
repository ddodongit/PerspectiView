package com.example.backend.modules.account;

import com.example.backend.modules.team.Enrollment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// ORM - Object Relation Mapping


@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = true)
    private String userImage;

    @Column(nullable = false)
    private String userNickname;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String userPhone;

    @Column(nullable = true)
    private String userInfo;

    @CreationTimestamp
    private Timestamp createAt;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    // 팀 참가신청 리스트
    @OneToMany(mappedBy = "user")
    private List<Enrollment> enrollments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<UserAuthority> authorities = new ArrayList<>();

    public void addAuthority(UserAuthority userAuthority) {
        authorities.add(userAuthority);
    }

    public void changeUser(User user){
        this.userImage = user.getUserImage();
        this.userNickname = user.getUserNickname();
        this.email = user.getEmail();
        this.userPhone = user.getUserPhone();
        this.userInfo = user.getUserInfo();
    }

    @Builder(builderMethodName = "requestBuilder")
    public User(String userNickname, String image, String email, String userPhone, String userInfo) {
        this.userNickname = userNickname;
        this.userImage = image;
        this.email = email;
        this.userPhone = userPhone;
        this.userInfo = userInfo;
    }
    @Builder
    public User(String username, String userNickname, String email, String provider, String providerId, List<UserAuthority> roles) {
        this.username = username;
        this.userNickname = userNickname;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }
}
