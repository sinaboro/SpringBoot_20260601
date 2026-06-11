package com.example.jpamember.repository;

import com.example.jpamember.entity.JpaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<JpaMember, Long> {

    // ─── 자동 제공 메서드 (JpaRepository 상속) ────────────
    // findAll() - 전체 조회 (List<JpaMember>)
    // findById(Long id) - PK 조회 (Optional<JpaMember>)
    // save(JpaMember m) - 저장/수정 (id 없으면 INSERT, 있으면 UPDATE)
    // deleteById(Long id) - PK 삭제
    // existsById(Long id) - 존재 여부 확인
    // count() - 전체 건수

    // ─── 쿼리 메서드 (메서드 이름으로 SQL 자동 생성) ────────
    // 이름 검색 (LIKE %keyword%) → SELECT * FROM jpa_member WHERE name LIKE '%?%'
    List<JpaMember> findByNameContaining(String keyword);

    // 이메일로 단건 조회 (중복 체크용) → SELECT * FROM jpa_member WHERE email = ?
    Optional<JpaMember> findByEmail(String email);

    // 이름으로 정렬 조회 → SELECT * FROM jpa_member ORDER BY name desc
    List<JpaMember> findAllByOrderByIdDesc();




}
