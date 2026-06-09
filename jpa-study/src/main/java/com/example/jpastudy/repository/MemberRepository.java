package com.example.jpastudy.repository;

import com.example.jpastudy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// JpaRepository<엔티티 타입, PK 타입>
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 기본 CRUD는 자동 제공! 추가 메서드는 다음 챕터에서

    // ── 쿼리 메서드 ───────────────────────────
    Optional<Member> findByEmail(String email);
    List<Member> findByUsernameContaining(String keyword);
    List<Member> findByAgeGreaterThanEqual(int age);
    List<Member> findAllByOrderByUsernameAsc();

    // ── @Query (JPQL) ─────────────────────────
    // JPQL: 테이블명 대신 엔티티명, 컬럼명 대신 필드명 사용!
    @Query("SELECT m FROM Member m WHERE m.age >= :minAge AND m.age <= :maxAge")
    List<Member> findByAgeRange(@Param("minAge") int min, @Param("maxAge") int max);

    // ── Native Query (실제 SQL) ───────────────
    @Query(value = "SELECT * FROM members WHERE user_name LIKE %:kw%", nativeQuery = true)
    List<Member> searchByName(@Param("kw") String keyword);

    // ── 집계 쿼리 ─────────────────────────────
    @Query("SELECT AVG(m.age) FROM Member m")
    Double findAverageAge();
}
