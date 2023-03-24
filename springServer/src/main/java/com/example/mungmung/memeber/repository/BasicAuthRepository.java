package com.example.mungmung.memeber.repository;

import com.example.mungmung.memeber.entity.BasicAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicAuthRepository extends JpaRepository<BasicAuthentication,Long> {
}
