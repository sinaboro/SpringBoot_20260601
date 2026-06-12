package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.JpaMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserAccount implements UserDetails {

    // JpaMember를 내부에 보관 → getJpaMember()로 꺼낼 수 있음
    private final JpaMember jpaMember;

    // 생성자: JpaMember를 받아서 보관
    public UserAccount(JpaMember jpaMember) {
        this.jpaMember = jpaMember;
    }

    // ★ 핵심 메서드: Controller에서 현재 로그인 사용자의 Entity를 꺼낼 때 사용
    // 사용 예: userAccount.getJpaMember().getId() → 로그인 사용자의 DB id 조회
    public JpaMember getJpaMember() {
        return jpaMember;
    }

    // 로그인 아이디 반환 (Security가 인증에 사용)
    @Override
    public String getUsername() {
        return jpaMember.getUsername();
    }
    // BCrypt 암호화된 비밀번호 반환 (Security가 비교에 사용)

    @Override
    public String getPassword() { return jpaMember.getPassword(); }
    // 권한 목록 반환 — ROLE_USER, ROLE_ADMIN 등
    // SimpleGrantedAuthority: 문자열 권한을 GrantedAuthority 객체로 변환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(jpaMember.getRole()));
    }
    // 계정 상태 메서드 — 교육용으로 모두 true 반환 (잠금·만료 기능 미사용)
    // 실무에서는 비활성화된 계정, 잠긴 계정 등을 false로 처리합니다
    @Override public boolean isAccountNonExpired() { return true; } // 계정 만료 여부
    @Override public boolean isAccountNonLocked() { return true; } // 계정 잠금 여부
    @Override public boolean isCredentialsNonExpired() { return true; } // 비밀번호 만료 여부
    @Override public boolean isEnabled() { return true; } // 계정 활성화 여부
}
