package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.entity.Authentication;
import com.example.mungmung.memeber.entity.BasicAuthentication;
import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.repository.BasicAuthRepository;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.memeber.request.SignUpRequest;
import com.example.mungmung.memeber.service.security.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.support.PageableUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BasicAuthRepository basicAuthRepository;

    @Autowired
    RedisService redisService;

    @Override
    public Boolean SignUp(SignUpRequest request) {
        try{
            final Member member = request.toMember();

            memberRepository.save(member);
            final BasicAuthentication auth = new BasicAuthentication(member,
                    Authentication.BASIC_AUTH, request.getPassword());

            basicAuthRepository.save(auth);
            return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Map<Boolean,String> Login(String email,String password){
        try{
            Map<Boolean,String> loginResult = new HashMap<>();
            Optional<Member> maybeMember = memberRepository.findByEmail(email);

            if(maybeMember.isEmpty()){
                loginResult.put(false,"이메일이 존재 하지 않습니다 다시 확인 해주세요");
            }else{
                Member member = maybeMember.get();

                System.out.println("member Email : " + member.getEmail());
                System.out.println("request Email : " + email);
                System.out.println("request Password : " + password);

                if (!member.isRightPassword(password)) {
                    // 패스워드가 잘못 된 경우
                    loginResult.put(false,"패스워드가 다릅니다. 확인해주세요");

                    throw new RuntimeException("아이디 또는 패스워드가 잘못되었습니다");
                }

                UUID userToken = UUID.randomUUID();

                redisService.deleteByKey(userToken.toString());
                redisService.setKeyAndValue(userToken.toString(), member.getId());

                System.out.println("유저토큰: " + userToken.toString());
                System.out.println("레디스 유저토큰으로 멤버아이디 구하기: " + redisService.getValueByKey(userToken.toString()));
                System.out.println("유저토큰:" + userToken.toString() + " 멤버아이디:" + member.getId());

                loginResult.put(true, userToken.toString());

            }

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


}
