package com.example.mungmung.memeber.controller;

import com.example.mungmung.memeber.request.SignInRequest;
import com.example.mungmung.memeber.request.SignUpRequest;
import com.example.mungmung.memeber.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/sign-up")
    public Boolean signUp(@RequestBody SignUpRequest request) {
        log.info("signUp(): " + request);

        return memberService.signUp(request);
    }

    @PostMapping("/sign-in")
    public Map<Boolean,String> signIn(@RequestBody SignInRequest request) {
        log.info("signIn(): " + request);

        return memberService.signIn(request);
    }

    @GetMapping("/email-check/{email}")
    public Boolean emailValidation(@PathVariable("email") String email) {
        log.info("emailValidation(): " + email);

        return memberService.checkEmailDuplication(email);
    }

    @GetMapping("/nickname-check/{nickname}")
    public Boolean nicknameValidation(@PathVariable("nickname") String nickname) {
        log.info("nicknameValidation(): " + nickname);

        return memberService.checkNicknameDuplication(nickname);
    }
}
