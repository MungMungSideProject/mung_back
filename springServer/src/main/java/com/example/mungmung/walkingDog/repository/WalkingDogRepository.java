package com.example.mungmung.walkingDog.repository;

import com.example.mungmung.walkingDog.entity.WalkingDog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingDogRepository extends JpaRepository<WalkingDog, Long> {
}
