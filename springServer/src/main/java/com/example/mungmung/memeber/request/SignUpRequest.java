package com.example.mungmung.memeber.request;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.entity.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    private String email;
    private String password;
    private String nickName;
    private String memberType;

    public Member toMember(){
        MemberType valueOfMemberType = MemberType.valueOfMemberType(this.memberType);

        return new Member(email,nickName,valueOfMemberType);
    }
}
