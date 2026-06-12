package com.example.jpasecurity.service;

import com.example.jpasecurity.dto.RegisterDto;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // ★ 회원가입 처리
    // RegisterDto(폼 데이터) → JpaMember(Entity) 변환 후 DB 저장
    @Transactional
    public void register(RegisterDto dto) {
        // 아이디 중복 체크 — 이미 존재하면 예외 발생
        if (memberRepository.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        // @Builder 패턴으로 JpaMember 객체 생성
//        JpaMember member = JpaMember.builder()
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .phone(dto.getPhone())
//                .username(dto.getUsername())
//                // ★ BCrypt 암호화: 1234 → $2a$10$abc... 형태의 해시값으로 변환
//                // encode()는 호출할 때마다 다른 해시값이 나옵니다 (salt 포함)
//                .password(passwordEncoder.encode(dto.getPassword()))
//                .role("ROLE_USER") // 기본 권한 설정
//                .build();

        JpaMember member = JpaMember.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .username(dto.getUsername())
                .role("ROLE_USER")
                .password(dto.getPassword())
                .build();
        
        memberRepository.save(member);
    }

}
