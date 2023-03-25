package com.example.mungmung.memeber.request;

import com.example.mungmung.memeber.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    private String email;
    private String password;
    private String nickname;

    public Member toMember(){
        return new Member(email,nickname);
    }
}
