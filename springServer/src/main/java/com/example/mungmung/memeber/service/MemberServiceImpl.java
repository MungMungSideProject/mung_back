package com.example.mungmung.memeber.service;

import com.example.mungmung.security.entity.Authentication;
import com.example.mungmung.security.entity.BasicAuthentication;
import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.security.repository.AuthRepository;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.memeber.request.SignInRequest;
import com.example.mungmung.memeber.request.SignUpRequest;
import com.example.mungmung.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    RedisService redisService;

    @Override
    public Boolean signUp(SignUpRequest request) {
        try{
            final Member member = request.toMember();

            memberRepository.save(member);
            final BasicAuthentication auth = new BasicAuthentication(member,
                    Authentication.BASIC_AUTH, request.getPassword());

            authRepository.save(auth);
            return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Map<Boolean,String> signIn(SignInRequest request){
        try{
            Map<Boolean,String> loginResult = new HashMap<>();
            Optional<Member> maybeMember = memberRepository.findByEmail(request.getEmail());

            if(maybeMember.isEmpty()){
                loginResult.put(false,"존재하지 않는 계정입니다.");
                return loginResult;
            }

            Member member = maybeMember.get();

            if (!member.isRightPassword(request.getPassword())) {
                loginResult.put(false,"패스워드가 일치하지 않습니다.");
                return loginResult;
            }

            String userToken = UUID.randomUUID().toString();

            redisService.deleteByKey(userToken);
            redisService.setKeyAndValue(userToken, member.getId());
            loginResult.put(true, userToken);

            return loginResult;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Boolean checkEmailDuplication(String email){
        try {
            Optional<Member> maybeMember = memberRepository.findByEmail(email);
            if(maybeMember.isEmpty()){
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
            throw new RuntimeException("오류 발생"  + e);
        }
    }

    @Override
    public Boolean checkNicknameDuplication(String nickname) {

        Optional<Member> maybeMember = memberRepository.findByNickname(nickname);

        if(maybeMember.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean signOut(String token) {

        redisService.deleteByKey(token);

        Long memberId = redisService.getValueByKey(token);

        if( memberId != null ) {
            System.out.println("로그아웃 안 됨");
            return false;
        }
        return true;
    }
}
