package com.example.mungmung.memeber.service.profile;

import com.example.mungmung.memeber.request.ProfileRequest;

public interface ProfileService {

    Boolean registerProfileInfo(ProfileRequest request , String Token);
}
