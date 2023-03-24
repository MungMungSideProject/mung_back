package com.example.mungmung.memeber.service.profile;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.entity.MemberPets;
import com.example.mungmung.memeber.entity.MemberProfile;
import com.example.mungmung.memeber.repository.MemberPetRepository;
import com.example.mungmung.memeber.repository.MemberProfileRepository;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.memeber.request.ProfileRequest;
import com.example.mungmung.memeber.service.security.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ProfileServiceImpl implements ProfileService{

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberPetRepository memberPetRepository;
    @Autowired
    MemberProfileRepository profileRepository;
    @Autowired
    RedisService redisService;

    @Override
    public Boolean registerProfileInfo(ProfileRequest request , String token) {
        try {
            Long id = redisService.getValueByKey(token);
            Member member = memberRepository.findByMemberId(id);

            MemberProfile profile = request.toMemberProfile();
            member.setMemberProfile(profile);
            memberRepository.save(member);
            profileRepository.save(profile);

            MemberPets memberPets = new MemberPets();
            memberPets.setMemberProfile(profile);
            for (int idx = 0; idx < profile.getPets().size(); idx++) {
                memberPets.setName(profile.getPets().get(idx).getName());
                memberPets.setAge(profile.getPets().get(idx).getAge());
                memberPets.setDogType(profile.getPets().get(idx).getDogType());
            }
            memberPetRepository.save(memberPets);
            return true;
        }catch (Exception e ){
            throw new RuntimeException("등록 실패! " +e.toString());
        }
    }
}
