package com.example.mungmung.mungWiki.service;

import com.example.mungmung.mungWiki.entity.*;
import com.example.mungmung.mungWiki.repository.DogStatusRepository;
import com.example.mungmung.mungWiki.repository.MungWikiRepository;
import com.example.mungmung.mungWiki.repository.WikiDocumentRepository;
import com.example.mungmung.mungWiki.repository.WikiImagesRepository;
import com.example.mungmung.mungWiki.request.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Service
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

                        FileOutputStream writer = new FileOutputStream("../mung-front/src/assets/wikiDog/" + uploadImageName);

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

    @Override
    public Map<String, Object> readWikiInfo(DogType dogType) {
        try {
            Map<String, Object> returnData = new HashMap<>();

            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(dogType);

            if (maybeMungWiki.isEmpty()) {
                returnData.put("결과", "해당 견종은 아직 등록 되지 않았습니다");
            } else {
                MungWiki MungWikiByDogType = maybeMungWiki.get();

                List<String> imagesNameList = setImageNameList(MungWikiByDogType.getWikiImages());
                Long TotalStatus = calculateTotalStatus(MungWikiByDogType.getDogStatus());
                returnData.put("sheddingLevel", MungWikiByDogType.getDogStatus().getSheddingLevel());
                returnData.put("intelligenceLevel", MungWikiByDogType.getDogStatus().getIntelligenceLevel());
                returnData.put("sociabilityLevel", MungWikiByDogType.getDogStatus().getSociabilityLevel());
                returnData.put("activityLevel", MungWikiByDogType.getDogStatus().getActivityLevel());
                returnData.put("indoorAdaptLevel", MungWikiByDogType.getDogStatus().getIndoorAdaptLevel());
                returnData.put("documentation", MungWikiByDogType.getWikiDocument().getDocument());
                returnData.put("totalStatus", TotalStatus);
                returnData.put("imagesNameList", imagesNameList);

            }
            return returnData;

        } catch (Exception e) {
            throw e;
        }

    }

    private Long calculateTotalStatus(DogStatus dogStatus) {
        Long totalStatus;
        Long StatusSum = dogStatus.getActivityLevel() + dogStatus.getSociabilityLevel() + dogStatus.getSheddingLevel()
                + dogStatus.getIntelligenceLevel() + dogStatus.getIndoorAdaptLevel();
        System.out.println("StatusSum : " + StatusSum);

        totalStatus = StatusSum / 5;
        System.out.println("totalStatus : " + totalStatus);


        return totalStatus;
    }

    private List<String> setImageNameList(List<WikiImages> wikiImages) {

        List<String> imageNameList = new ArrayList<>();
        for (int i = 0; i <= wikiImages.size(); i++) {
            imageNameList.set(i, wikiImages.get(i).getUploadImageName());
        }
        return imageNameList;
    }

    @Override
    public String ModifyMungWikiWithImage(RegisterRequest request, List<MultipartFile> images) {
        try {
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(DogType.valueOf(request.getDogType()));

            if (maybeMungWiki.isEmpty()) {
                return "해당 견종은 아직 미등록 상태 입니다!, 먼저 정보를 등록해주세요 관리자님!";
            } else {
                MungWiki mungWiki = maybeMungWiki.get();

                boolean result = modifyMungWikiLogic(mungWiki, request);
                if (!result) {
                    return "오류로 인한 수정 실패!";
                } else {
                    try {
                        List<String> oldImageFileNames = new ArrayList<>();
                        List<String> newImageFileNames = new ArrayList<>();
                        List<WikiImages> wikiImagesList = wikiImagesRepository.findImagesByWikiId(mungWiki.getId());
                        for (int i = 0; i < mungWiki.getWikiImages().size(); i++) {
                            oldImageFileNames.set(i, mungWiki.getWikiImages().get(i).getOriginalImageName());
                        }
                        for (int i = 0; i < images.size(); i++) {
                            newImageFileNames.set(i, images.get(i).getOriginalFilename());
                        }
                        for (int i = 0; i < oldImageFileNames.size(); i++) {
                            File webfile = new File("../mung-front/src/assets/wikiDog/" + URLDecoder.decode(oldImageFileNames.get(i), "UTF-8"));
                            webfile.delete();
                        }
                        for (int i = 0; i < newImageFileNames.size(); i++) {
                            FileOutputStream writer = new FileOutputStream("../mung-front/src/assets/wikiDog/" + newImageFileNames.get(i));
                            writer.write(images.get(i).getBytes());
                            writer.close();

                            UUID uuid = UUID.randomUUID();

                            WikiImages tmpWikiImage = new WikiImages();
                            tmpWikiImage.setOriginalImageName(newImageFileNames.get(i));
                            tmpWikiImage.setUploadImageName(uuid + newImageFileNames.get(i));
                            wikiImagesList.set(i, tmpWikiImage);
                            wikiImagesRepository.save(tmpWikiImage);
                            mungWiki.setWikiImages(wikiImagesList);
                            mungWikiRepository.save(mungWiki);
                        }

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return "수정 성공!";
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String ModifyMungWikiWithoutImage(RegisterRequest request) {
        try {
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(DogType.valueOf(request.getDogType()));

            if (maybeMungWiki.isEmpty()) {
                return "해당 견종은 아직 미등록 상태 입니다!, 먼저 정보를 등록해주세요 관리자님!";
            } else {
                MungWiki mungWiki = maybeMungWiki.get();

                boolean result = modifyMungWikiLogic(mungWiki, request);
                if (!result) {
                    return "오류로 인한 수정 실패!";
                } else {
                    return "위키 정보 수정 완료!";
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String deleteMungWikiInfo(String dogType) {
        try {
            DogType valueOfDogStatus = DogType.valueOfDogStatus(dogType);
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(valueOfDogStatus);

            if (maybeMungWiki.isEmpty()) {
                return "해당 견종은 아직 등록이 안되어있습니다.";
            } else {
                mungWikiRepository.delete(maybeMungWiki.get());
                return "강이지 정보 초기화가 완료 되었습니다.";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean modifyMungWikiLogic(MungWiki mungWiki, RegisterRequest request) {
        try {
            DogStatus dogStatus = mungWiki.getDogStatus();
            WikiDocument document = mungWiki.getWikiDocument();
            if (request.getIntelligenceLevel() != null) {
                dogStatus.setIntelligenceLevel(request.getIntelligenceLevel());
            }
            if (request.getSheddingLevel() != null) {
                dogStatus.setSheddingLevel(request.getSheddingLevel());
            }
            if (request.getIndoorAdaptLevel() != null) {
                dogStatus.setIndoorAdaptLevel(request.getIndoorAdaptLevel());
            }
            if (request.getSociabilityLevel() != null) {
                dogStatus.setSociabilityLevel(request.getSociabilityLevel());
            }
            if (request.getActivityLevel() != null) {
                dogStatus.setActivityLevel(request.getActivityLevel());
            }
            if (request.getDocumentation() != null) {
                document.setDocument(request.getDocumentation());
            }

            dogStatusRepository.save(dogStatus);
            wikiDocumentRepository.save(document);
            mungWiki.setWikiDocument(document);
            mungWiki.setDogStatus(dogStatus);
            mungWikiRepository.save(mungWiki);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
