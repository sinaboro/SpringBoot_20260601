package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.JpaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// JpaRepository<엔티티 클래스, PK 타입> 상속만 하면
// findAll(), findById(), save(), deleteById() 등이 자동으로 제공됩니다
public interface MemberRepository extends JpaRepository<JpaMember, Long> {
    // 이름 검색 — name 컬럼에서 keyword를 포함한 결과 반환
    // 동명이인이 여러 명일 수 있으므로 List로 반환
    // JPA가 메서드 이름을 분석해 SQL 자동 생성:
    // → SELECT * FROM jpa_member WHERE name LIKE '%keyword%'
    List<JpaMember> findByNameContaining(String keyword);

    // ★ 로그인 ID로 조회 — UserDetailsServiceImpl에서 호출
    // Optional: 해당 username이 없을 경우 null 대신 Optional.empty() 반환
    // → .orElseThrow()로 예외 처리 가능
    Optional<JpaMember> findByUsername(String username);

    // 아이디 중복 체크 — 회원가입 시 이미 사용 중인지 확인
    // true: 이미 존재함 / false: 사용 가능
    boolean existsByUsername(String username);

    // 이메일 중복 체크
    boolean existsByEmail(String email);
}