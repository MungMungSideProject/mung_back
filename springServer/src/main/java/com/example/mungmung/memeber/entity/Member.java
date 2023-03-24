package com.example.mungmung.memeber.entity;

import com.example.mungmung.security.entity.Authentication;
import com.example.mungmung.security.entity.BasicAuthentication;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mmeber_ id")
    private Long id;

    @Column
    private String email;

    @Column
    private String nickName;

    @OneToOne(fetch =  FetchType.LAZY, orphanRemoval = true)
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Authentication> authentications = new HashSet<>();

    public Member(String email,String nickName){
        this.email = email;
        this.nickName = nickName;
    }
    public Member(String email,String nickName, MemberProfile memberProfile ){
        this.email = email;
        this.nickName = nickName;
        this.memberProfile =memberProfile;
    }

    private Optional<Authentication> findBasicAuthentication() {
        return authentications
                .stream()
                .filter(auth -> auth instanceof BasicAuthentication)
                .findFirst();
    }

    public boolean isRightPassword(String plainToCheck) {
        final Optional<Authentication> maybeBasicAuth = findBasicAuthentication();

        if (maybeBasicAuth.isPresent()) {
            final BasicAuthentication auth = (BasicAuthentication) maybeBasicAuth.get();
            return auth.isRightPassword(plainToCheck);
        }

        return false;
    }
}
