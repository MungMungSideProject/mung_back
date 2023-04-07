package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.request.SignInRequest;
import com.example.mungmung.memeber.request.SignUpRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface MemberService {

    Boolean signUp(SignUpRequest request);

    Map<Boolean,String> signIn(SignInRequest request);

    Boolean checkEmailDuplication(String email);

    Boolean checkNicknameDuplication(String nickname);

    Boolean signOut(String token);
}
