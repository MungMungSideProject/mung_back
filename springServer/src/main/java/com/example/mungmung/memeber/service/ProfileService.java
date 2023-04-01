package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.entity.Pet;
import com.example.mungmung.memeber.request.PetRegisterRequest;

import java.util.List;
import java.util.Set;

public interface ProfileService {

//    Boolean registerProfileInfo(ProfileRequest request , String Token);

    Boolean registerPetInfo(PetRegisterRequest request, String Token);
}
