package com.example.mungmung.memeber.service;

import com.example.mungmung.memeber.request.ProfileRequest;

public interface ProfileService {

    Boolean registerProfileInfo(ProfileRequest request , String Token);
}
