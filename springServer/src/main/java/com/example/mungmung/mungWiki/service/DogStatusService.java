package com.example.mungmung.mungWiki.service;

import com.example.mungmung.mungWiki.request.StatusRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DogStatusService {
    Map<Boolean,String> gradeStatusLevelFromMember(StatusRequest statusRequest, String token);

}
