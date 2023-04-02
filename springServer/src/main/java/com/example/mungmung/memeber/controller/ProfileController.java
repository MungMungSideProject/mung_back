package com.example.mungmung.memeber.controller;

import com.example.mungmung.memeber.request.PetRegisterRequest;
import com.example.mungmung.memeber.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/register-pet")
    public Boolean registerPetInfo(@RequestBody PetRegisterRequest request, @RequestParam("token") String token) {
        log.info("registerPetInfo(): " + request);
        return profileService.registerPetInfo(request, token);
    }
}
