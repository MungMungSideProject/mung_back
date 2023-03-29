package com.example.mungmung.mungWiki.service;

import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.request.RegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Service
public interface MungWikiService {

    String registerWiki(RegisterRequest request, List<MultipartFile> images);
    Map<String,Object> readWikiInfo(DogType dogType);
    String ModifyMungWikiWithImage(RegisterRequest request, List<MultipartFile> images);
    String ModifyMungWikiWithoutImage(RegisterRequest request);
    String deleteMungWikiInfo(String dogType);
}
