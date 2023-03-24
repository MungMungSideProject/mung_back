package com.example.mungmung.memeber.entity;

import com.example.mungmung.mungWiki.entity.DogType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum MemberType {
    ADMIN("관리자"),GENERAL_MEMBER("일반회원"),NAVER("네이버"),KAKAO("카카오");

    final private String MemberType;


    public static MemberType valueOfMemberType(String MemberType) {
        return Arrays.stream(values())
                .filter(value -> value.MemberType.equals(MemberType))
                .findAny()
                .orElse(null);
    }
}
