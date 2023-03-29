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
    public String gradeStatusLevelFromMember(StatusRequest statusRequest , String token) {

        try {
            Long id = redisService.getValueByKey(token);
            if(id.equals(null))return "회원 정보 오류";

            Member Member = memberRepository.findByMemberId(id);

            DogType dogType = DogType.valueOfDogStatus(statusRequest.getDogType());
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(dogType);

            if(maybeMungWiki.isPresent()){
                MungWiki mungWiki = maybeMungWiki.get();

                DogStatus dogStatus = mungWiki.getDogStatus();
                Long GranterCnt = dogStatus.getNumOfGranter();
                dogStatus.setNumOfGranter(GranterCnt);

                dogStatus.setSheddingLevel(calDogStatusAverage(GranterCnt,dogStatus.getSheddingLevel(),statusRequest.getSheddingLevel()));
                dogStatus.setActivityLevel(calDogStatusAverage(GranterCnt,dogStatus.getActivityLevel(),statusRequest.getActivityLevel()));
                dogStatus.setSociabilityLevel(calDogStatusAverage(GranterCnt,dogStatus.getSociabilityLevel(),statusRequest.getSociabilityLevel()));
                dogStatus.setIntelligenceLevel(calDogStatusAverage(GranterCnt,dogStatus.getIntelligenceLevel(),statusRequest.getIntelligenceLevel()));
                dogStatus.setIndoorAdaptLevel(calDogStatusAverage(GranterCnt,dogStatus.getIndoorAdaptLevel(),statusRequest.getIndoorAdaptLevel()));

                dogStatusRepository.save(dogStatus);

                return "점수 부여 성공,";

            }else{
                return "아직 해당 견종 등록이 안되어 있어 점수 부여가 불가 합니다.";
            }


        }catch (Exception e){
            throw e;
        }
    }

    private Long calDogStatusAverage(Long granterCnt, Long totalStatus , Long plusStatus){
        Long average = totalStatus + plusStatus;
        average /= granterCnt;

        return average;
    }
}
