package com.example.mungmung.memeber.request;

import com.example.mungmung.memeber.entity.MemberPets;
import com.example.mungmung.memeber.entity.MemberProfile;
import com.example.mungmung.mungWiki.entity.DogType;

import java.util.ArrayList;
import java.util.List;

public class ProfileRequest {
    private String phoneNumber;
    private List<String> petName;
    private List<Long> petAge;
    private List<String> petType;

    public MemberPets toMemberPets(int idx){
        DogType dogType = DogType.valueOfDogStatus(petType.get(idx));
        return new MemberPets(petName.get(idx),petAge.get(idx),dogType);
    }

    public MemberProfile toMemberProfile(){
        List<MemberPets> petsList = new ArrayList<>();
        for (int i = 0; i < petName.size(); i++) {

            MemberPets memberPets = toMemberPets(i);
            petsList.add(memberPets);
        }

        return new MemberProfile(this.phoneNumber,petsList);
    }
}
