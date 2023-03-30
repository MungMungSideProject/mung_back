package com.example.mungmung.mungWiki.service;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.mungWiki.entity.DogStatus;
import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.entity.MungWiki;
import com.example.mungmung.mungWiki.repository.DogStatusRepository;
import com.example.mungmung.mungWiki.repository.MungWikiRepository;
import com.example.mungmung.mungWiki.request.StatusRequest;
import com.example.mungmung.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DogStatusServiceImpl implements DogStatusService{

    @Autowired
    private DogStatusRepository dogStatusRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MungWikiRepository mungWikiRepository;

    @Override
    public Map<Boolean,String> gradeStatusLevelFromMember(StatusRequest statusRequest , String token) {

        try {
            Map<Boolean,String> result = new HashMap<>();
            // 비정상 로그인 거르기
            Long id = redisService.getValueByKey(token);
            if(id.equals(null)){result.put(false , "there is an unusual approach"); return result;}

            // 강아지 키우기 여부 판단,
            DogType requestDogType = DogType.valueOfDogStatus(statusRequest.getDogType());
            Boolean isMemberBreedDog = false;
            Member Member = memberRepository.findByMemberId(id);
            if(Member.getMemberProfile().getPets().size() == 0 ){
                result.put(false , "this member not breed pet");
                return result;
            }
            for (int i = 0; i < Member.getMemberProfile().getPets().size(); i++) {
                DogType memberDogType = Member.getMemberProfile().getPets().get(i).getDogType();
                if(memberDogType == requestDogType){
                    isMemberBreedDog = true;
                    break;
                }
            }
            // 위키 등록 여부 판단,
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(requestDogType);

            // 실제 점수 할당 로직.
            if(maybeMungWiki.isPresent() && isMemberBreedDog){
                MungWiki mungWiki = maybeMungWiki.get();

                DogStatus dogStatus = mungWiki.getDogStatus();
                Long GranterCnt = dogStatus.getNumOfGranter() + 1;
                dogStatus.setNumOfGranter(GranterCnt);

                dogStatus.setSheddingLevel(calDogLevelAverage(GranterCnt,dogStatus.getSheddingLevel(),statusRequest.getSheddingLevel()));
                dogStatus.setActivityLevel(calDogLevelAverage(GranterCnt,dogStatus.getActivityLevel(),statusRequest.getActivityLevel()));
                dogStatus.setSociabilityLevel(calDogLevelAverage(GranterCnt,dogStatus.getSociabilityLevel(),statusRequest.getSociabilityLevel()));
                dogStatus.setIntelligenceLevel(calDogLevelAverage(GranterCnt,dogStatus.getIntelligenceLevel(),statusRequest.getIntelligenceLevel()));
                dogStatus.setIndoorAdaptLevel(calDogLevelAverage(GranterCnt,dogStatus.getIndoorAdaptLevel(),statusRequest.getIndoorAdaptLevel()));

                dogStatusRepository.save(dogStatus);

                result.put(true,"grant Status success,");
                return result;

            }else if (maybeMungWiki.isEmpty()){
                result.put(false,"NO Wiki information");
                return result;
            } else {
                result.put(false,"member not breed this DogType : " + requestDogType.toString());
                return result;
            }


        }catch (Exception e){
            throw e;
        }
    }

    public Long calDogLevelAverage(Long granterCnt, Long totalStatus , Long plusStatus){
        Long average = totalStatus + plusStatus;
        average /= granterCnt;

        return average;
    }

    public Long calDogStatusAverage(DogStatus dogStatus){
        Long TotalStatus;
        Long returnAverage;
        TotalStatus = dogStatus.getActivityLevel();
        TotalStatus +=dogStatus.getSheddingLevel();
        TotalStatus +=dogStatus.getIntelligenceLevel();
        TotalStatus +=dogStatus.getSociabilityLevel();
        TotalStatus +=dogStatus.getIndoorAdaptLevel();

        returnAverage = TotalStatus/5;
        return returnAverage;
    }
}
