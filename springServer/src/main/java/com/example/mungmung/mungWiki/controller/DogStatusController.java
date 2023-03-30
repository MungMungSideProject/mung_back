package com.example.mungmung.mungWiki.controller;

import com.example.mungmung.mungWiki.request.StatusRequest;
import com.example.mungmung.mungWiki.service.DogStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/DogStatus")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DogStatusController {


    @Autowired
    DogStatusService dogStatusService;


    @PostMapping("/greed_dog_status")
    public Map<Boolean,String> GreedDogStatus(@RequestBody StatusRequest statusRequest , @RequestParam("token") String token){

        return dogStatusService.gradeStatusLevelFromMember(statusRequest , token);
    }
}
