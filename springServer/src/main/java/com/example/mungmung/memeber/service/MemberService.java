package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.request.SignUpRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MemberService {

    Boolean SignUp(SignUpRequest request);

    Map<Boolean,String> Login(String email,String password);

    Boolean checkEmailDuplication(String email);
}
