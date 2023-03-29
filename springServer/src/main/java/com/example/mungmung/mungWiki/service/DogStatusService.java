package com.example.mungmung.mungWiki.service;

import com.example.mungmung.mungWiki.request.StatusRequest;
import org.springframework.stereotype.Service;

@Service
public interface DogStatusService {
    String gradeStatusLevelFromMember(StatusRequest statusRequest, String token);

}
