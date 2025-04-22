package com.appcenter.wnt.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "store_name")
    private String name;

    @Builder
    private Store(User user, String name){
        this.user = user;
        this.name = name;
    }

    public static Store of(User user, String name){
        return Store.builder().user(user).name(name).build();
    }
}
