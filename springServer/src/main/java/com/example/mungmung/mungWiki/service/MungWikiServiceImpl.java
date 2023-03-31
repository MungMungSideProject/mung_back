package com.example.mungmung.mungWiki.service;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.mungWiki.entity.*;
import com.example.mungmung.mungWiki.repository.*;
import com.example.mungmung.mungWiki.request.RegisterRequest;
import com.example.mungmung.security.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookMartRepository bookMartRepository;

    @Override
    public String registerWiki(RegisterRequest request, List<MultipartFile> images) {
        try {
            DogType dogType = DogType.valueOfDogStatus(request.getDogType());
            Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(dogType);
            if (maybeMungWiki.isPresent()) {
                return "already register dogType.";
            } else {
                MungWiki mungWiki = new MungWiki();

                // 1.status 설정
                DogStatus dogStatus = new DogStatus();
                dogStatus.setActivityLevel(request.getActivityLevel());
                dogStatus.setSheddingLevel(request.getSheddingLevel());
                dogStatus.setSociabilityLevel(request.getSociabilityLevel());
                dogStatus.setIntelligenceLevel(request.getIntelligenceLevel());
                dogStatus.setIndoorAdaptLevel(request.getIndoorAdaptLevel());
                dogStatus.setNumOfGranter(1L);
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
                return "new wiki register success!";
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
                returnData.put("result", "No wiki Information");
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
                return "No wiki Information";
            } else {
                MungWiki mungWiki = maybeMungWiki.get();

                boolean result = modifyMungWikiLogic(mungWiki, request);
                if (!result) {
                    return "failed modify please check error message";
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
                    return "modify Success!";
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
                return "No wiki Information";
            } else {
                MungWiki mungWiki = maybeMungWiki.get();

                boolean result = modifyMungWikiLogic(mungWiki, request);
                if (!result) {
                    return "failed modify please check error message";
                } else {
                    return "wiki information modify success!";
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
                return "wiki Information not exist";
            } else {
                mungWikiRepository.delete(maybeMungWiki.get());
                return "delete wiki info success!";
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
            throw e;
        }
    }

    @Override
    public List<Map<String,Object>> readWikiInfoLists(String token){
        final Integer PAGE_SIZE = 10;
        List<MungWiki> wikiList = mungWikiRepository.findListFirstPage(Pageable.ofSize(PAGE_SIZE));

        try{
            return getWikiInfoList (wikiList,token);
        }catch (Exception e){
            throw  e;
        }
    }

    @Override
    public List<Map<String,Object>> readWikiInfoLists(DogType lastDogType , String token){
        final Integer PAGE_SIZE = 10;
        Optional<MungWiki> mungWiki = mungWikiRepository.findByDogType(lastDogType);
        List<MungWiki> wikiList = mungWikiRepository.findListNextPage(Pageable.ofSize(PAGE_SIZE) , mungWiki.get().getId());

        try{
            return getWikiInfoList( wikiList,token);
        }catch (Exception e){
            throw  e;
        }

    }

    private List<Map<String, Object>> getWikiInfoList(List<MungWiki> wikiList , String token) {
        List<Map<String,Object>> responseData = new ArrayList<>();
        for (int i = 0; i < wikiList.size(); i++) {
            DogStatusServiceImpl dogStatusService = new DogStatusServiceImpl();
            Long average = dogStatusService.calDogStatusAverage(wikiList.get(i).getDogStatus());
            Map<String,Object> dataFrom = new HashMap<>();
            dataFrom.put("dogType", wikiList.get(i).getDogType());
            dataFrom.put("thumbnailImg", wikiList.get(i).getWikiImages().get(0).getUploadImageName());
            dataFrom.put("averageStatus" , average);

            if(!token.equals("") || !token.isEmpty() || token != null){// 로그인 o
                Long id = redisService.getValueByKey(token);
                Optional<BookMark> maybeBookMark = bookMartRepository.findByMemberIDAndDogType(id,wikiList.get(i).getDogType());
                BookMark bookMark = maybeBookMark.get();
                dataFrom.put("bookmark" , bookMark.getMarkFlag());
            }
            responseData.add(dataFrom);
        }

        return responseData;
    }

}
