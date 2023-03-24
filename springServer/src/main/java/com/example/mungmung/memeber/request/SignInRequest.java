package com.example.mungmung.memeber.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SignInRequest {
    private final String email;
    private final String password;
}
