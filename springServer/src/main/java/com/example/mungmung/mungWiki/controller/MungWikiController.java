package com.example.mungmung.mungWiki.controller;

import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.request.RegisterRequest;
import com.example.mungmung.mungWiki.service.MungWikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/mungWiki")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MungWikiController {

    @Autowired
    private MungWikiService mungWikiService;

    @PostMapping(value =  "/register" , consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public String registerMungWiki(
            @RequestPart(value = "images") List<MultipartFile> images,
            @RequestPart(value = "info") RegisterRequest request
    ){

        return mungWikiService.registerWiki(request,images);
    }

    @GetMapping("read/{dogType}")
    public Map<String, Object> readWikiInfo(@PathVariable("dogType") String dogType){
        DogType valueOfDogType = DogType.valueOfDogStatus(dogType);

        return mungWikiService.readWikiInfo(valueOfDogType);
    }

    @PostMapping(value = "modify/withImg", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public String modifyWithImg(
            @RequestPart(value = "images") List<MultipartFile> images,
            @RequestPart(value = "info") RegisterRequest request){

        return mungWikiService.ModifyMungWikiWithImage(request,images);
    }

    @PostMapping("modify/withoutImg")
    public String modifyWithoutImg(@RequestBody RegisterRequest request){

        return mungWikiService.ModifyMungWikiWithoutImage(request);
    }

    @PostMapping("/read/wikiList")
    public List<Map<String,Object>> readWikiList(@RequestParam(value = "token" , required = false) String token){

        return mungWikiService.readWikiInfoLists(token);
    }

    @PostMapping("/read/nextPage/wikiList")
    public List<Map<String,Object>> readWikiList(
            @RequestParam(value = "token" , required = false) String token,
            @RequestParam(value = "dogType") String dogType
    ){
        DogType dogTypeValue = DogType.valueOfDogStatus(dogType);

        return mungWikiService.readWikiInfoLists(dogTypeValue, token);
    }

    @DeleteMapping("delete/{dogType}")
    public String deleteMungWiki(@PathVariable String dogType){
        return mungWikiService.deleteMungWikiInfo(dogType);
    }
}
