package com.appcenter.wnt.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Embedded
    private Nickname nickname;

    @Builder
    private User(@NonNull String nickname) {
        this.nickname = new Nickname(nickname);
    }

    public static User of(String nickname) {
        return User.builder().nickname(nickname).build();
    }
}
