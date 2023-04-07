package com.example.mungmung.memeber.repository;

import com.example.mungmung.memeber.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("select m from Member m join fetch m.authentications where m.email = :email")
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m join fetch m.authentications where m.nickname = :nickname")
    Optional<Member> findByNickname(String nickname);


    @Query("select m from Member m join fetch m.authentications where m.id = :id")
    Member findByMemberId(Long id);
}
