package com.example.jpasecurity.service;

import com.example.jpasecurity.dto.RegisterDto;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

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
                .role("ROLE_USER") // 기본 권한: 일반회원 (관리자는 DB에서 ROLE_ADMIN으로 변경)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        memberRepository.save(member);
    }

    // 전체 목록 조회
    // readOnly = true: 조회 전용 트랜잭션 → 성능 최적화 (Dirty Checking 비활성화)
    @Transactional(readOnly = true)
    public List<JpaMember> findAll() {
        return memberRepository.findAll();
    }

    // 이름 키워드 검색
    @Transactional(readOnly = true)
    public List<JpaMember> search(String keyword) {
        // keyword가 없으면 전체 목록 반환
        if (keyword == null || keyword.isBlank()) return findAll();
        return memberRepository.findByNameContaining(keyword);
    }

    // ID로 단건 조회 — 없으면 예외 발생
    @Transactional(readOnly = true)
    public JpaMember findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

    }

    // 수정 — name, email, phone만 변경 가능
    // ★ JPA Dirty Checking 활용: @Transactional 범위 안에서
    // Entity 필드를 변경하면 종료 시 자동으로 UPDATE SQL이 실행됩니다
    // → save() 를 별도로 호출하지 않아도 됩니다
    @Transactional
    public void update(Long id, String name, String email, String phone) {
        JpaMember member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        member.update(name, email, phone);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
