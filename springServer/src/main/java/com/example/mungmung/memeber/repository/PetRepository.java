package com.example.mungmung.memeber.repository;

import com.example.mungmung.memeber.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long> {
}
