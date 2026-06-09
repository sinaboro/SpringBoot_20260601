package com.example.jpamember.repository;

import com.example.jpamember.entity.JpaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<JpaMember, Long> {

    //이름으로 검색
    List<JpaMember> findByNameContaining(String name);

    //이메일로 검색
    List<JpaMember> findByEmailContaining(String email);
}
