package com.example.mungmung.memeber.repository;

import com.example.mungmung.memeber.entity.MemberPets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPetRepository extends JpaRepository<MemberPets,Long> {
}
