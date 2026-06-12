package com.example.jpasecurity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

// @Entity: 이 클래스가 DB 테이블과 1:1 매핑되는 JPA 엔티티임을 선언
@Entity
// @Table: 매핑할 실제 테이블명 지정 (생략 시 클래스명 소문자로 자동 지정)
@Table(name = "jpa_member")
@Getter @Setter // Lombok: getXxx() / setXxx() 자동 생성
@NoArgsConstructor // Lombok: 기본 생성자 자동 생성 (JPA 필수)
@AllArgsConstructor // Lombok: 전체 필드 생성자 자동 생성
@Builder // Lombok: 빌더 패턴 사용 가능 → MemberService에서 활용
public class JpaMember {
    // @Id: 기본키(PK) 지정
    // @GeneratedValue: 자동 증가 (MySQL AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 실제 이름 — 동명이인 허용 (UNIQUE 아님)
    // @NotBlank: null, 빈 문자열, 공백만 있는 경우 검증 실패
    @NotBlank(message = "이름을 입력해주세요")
    @Column(nullable = false, length = 50)
    private String name;

    // 이메일 — 중복 불가 (unique = true → DB UNIQUE 제약)
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // 전화번호 — 선택 입력 (nullable, UNIQUE 아님)
    // @Column에 nullable = false 를 붙이지 않으면 NULL 허용
    @Column(length = 20)
    private String phone;

    // 로그인 ID — 중복 불가 (unique = true)
    // ★ name(실제 이름)과 다른 개념! hong123, kim99 같은 아이디
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // BCrypt 암호화된 비밀번호 — 해시값이 저장됨 (길이 제한 없음)
    // Entity에 @NotBlank를 붙이지 않는 이유:
    // → 수정(editForm) 시 password를 전송하지 않기 때문에 검증 실패 발생
    // → 비밀번호 검증은 RegisterDto에서 처리 (PART 5 참고)
    @Column(nullable = false)
    private String password;

    // 권한 — ROLE_USER (일반회원) / ROLE_ADMIN (관리자)
    // Spring Security는 ROLE_ 접두사를 요구합니다
    @Column(nullable = false, length = 20)
    private String role;

    // 수정 전용 메서드 — name, email, phone만 변경 허용
    // username, password는 변경 불가 (보안상 별도 기능으로 분리 권장)
    // @Transactional 범위 안에서 호출하면 save() 없이도 UPDATE 됨 (Dirty Checking)
    public void update(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

}