package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.entity.Pet;
import com.example.mungmung.memeber.repository.PetRepository;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.memeber.request.PetRegisterRequest;
import com.example.mungmung.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    RedisService redisService;

//    @Override
//    public Boolean registerProfileInfo(ProfileRequest request, String token) {
//        try {
//            Long id = redisService.getValueByKey(token);
//            Member member = memberRepository.findByMemberId(id);
//
//            MemberProfile profile = request.toMemberProfile();
//            member.setMemberProfile(profile);
//            memberRepository.save(member);
//            profileRepository.save(profile);
//
//            MemberPets memberPets = new MemberPets();
//            memberPets.setMemberProfile(profile);
//            for (int idx = 0; idx < profile.getPets().size(); idx++) {
//                memberPets.setName(profile.getPets().get(idx).getName());
//                memberPets.setAge(profile.getPets().get(idx).getAge());
//                memberPets.setDogType(profile.getPets().get(idx).getDogType());
//            }
//            memberPetRepository.save(memberPets);
//            return true;
//        }catch (Exception e ){
//            throw new RuntimeException("등록 실패! " +e.toString());
//        }
//    }

    @Override
    public Boolean registerPetInfo(PetRegisterRequest request, String token) {
        Long id = redisService.getValueByKey(token);
        if(id == null) {
            System.out.println("토큰 없음");
            return null;
        }
        Optional<Member> maybeMember = memberRepository.findById(id);

        if(maybeMember.isEmpty()){
            System.out.println("멤버 없어용");
            return null;
        }
        Member member = maybeMember.get();

        Pet newPet = new Pet(member, request.getName(), request.getAge(), request.enumDogType());
        petRepository.save(newPet);

        return true;
    }
}
