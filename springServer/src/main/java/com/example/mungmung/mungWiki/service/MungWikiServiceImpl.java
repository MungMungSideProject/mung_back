package com.example.mungmung.mungWiki.service;

import com.example.mungmung.mungWiki.entity.*;
import com.example.mungmung.mungWiki.repository.DogStatusRepository;
import com.example.mungmung.mungWiki.repository.MungWikiRepository;
import com.example.mungmung.mungWiki.repository.WikiDocumentRepository;
import com.example.mungmung.mungWiki.repository.WikiImagesRepository;
import com.example.mungmung.mungWiki.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MungWikiServiceImpl implements MungWikiService {

    @Autowired
    private MungWikiRepository mungWikiRepository;

    @Autowired
    private DogStatusRepository dogStatusRepository;

    @Autowired
    private WikiDocumentRepository wikiDocumentRepository;

    @Autowired
    private WikiImagesRepository wikiImagesRepository;

    @Override
    public String registerWiki(RegisterRequest request, List<MultipartFile> images) {
        try {
            DogType dogType = DogType.valueOfDogStatus(request.getDogType());
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(dogType);
            if (maybeMungWiki.isPresent()) {
                return "이미 등록이 완료된 강아지 입니다.";
            } else {
                MungWiki mungWiki = new MungWiki();

                // 1.status 설정
                DogStatus dogStatus = new DogStatus();
                dogStatus.setActivityLevel(request.getActivityLevel());
                dogStatus.setSheddingLevel(request.getSheddingLevel());
                dogStatus.setSociabilityLevel(request.getSociabilityLevel());
                dogStatus.setIntelligenceLevel(request.getIntelligenceLevel());
                dogStatus.setIndoorAdaptLevel(request.getIndoorAdaptLevel());

                mungWiki.setDogStatus(dogStatus);
                dogStatusRepository.save(dogStatus);
                // 2.document 설정
                WikiDocument wikiDocument = new WikiDocument();
                wikiDocument.setDocument(request.getDocumentation());
                mungWiki.setWikiDocument(wikiDocument);
                wikiDocumentRepository.save(wikiDocument);

                // 3.image 설정
                List<WikiImages> wikiImages = new ArrayList<>();
                try {
                    for (int i = 0; i <= images.size(); i++) {
                        UUID uuid = UUID.randomUUID();
                        String originalFileName = images.get(i).getOriginalFilename();
                        String uploadImageName = uuid + originalFileName;

                        FileOutputStream writer = new FileOutputStream("../ztz_web/src/assets/products/uploadImg/" + uploadImageName);

                        WikiImages tmpWikiImage = new WikiImages();
                        tmpWikiImage.setOriginalImageName(originalFileName);
                        tmpWikiImage.setUploadImageName(uploadImageName);
                        wikiImages.set(i, tmpWikiImage);
                        writer.write(images.get(i).getBytes());
                        writer.close();
                        System.out.println("file upload success");

                        wikiImagesRepository.save(wikiImages.get(i));
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mungWiki.setWikiImages(wikiImages);

                mungWikiRepository.save(mungWiki);
                return "새로운 위키 등록 성공!";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }


}
