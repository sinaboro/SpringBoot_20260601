package com.example.jpasecurity.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// 회원가입 폼 데이터를 담는 전용 DTO (Data Transfer Object)
// @Valid 어노테이션과 함께 사용하면 아래 검증 어노테이션이 자동으로 실행됩니다
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    // 이름 — 필수 입력
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    // 이메일 — 필수 입력 + 형식 검증
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    // 전화번호 — 선택 입력
    // ^$ : 빈 문자열 허용 (선택 입력이므로)
    // | : OR 조건
    // ^01[0-9]-\d{3,4}-\d{4}$ : 010-1234-5678 또는 011-123-4567 형식
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "010-1234-5678 형식으로 입력하세요."
    )
    private String phone;

    // 로그인 아이디 — 영문·숫자만 허용, 4~20자
    // @Size: 문자열 길이 검증
    // @Pattern: 정규식으로 허용 문자 제한
    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "아이디는 영문, 숫자만 사용 가능합니다")
    private String username;

    // 비밀번호 — 필수 입력, 8자 이상
    // Entity 대신 DTO에서 검증하기 때문에 수정 화면에서 충돌이 발생하지 않습니다
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다")
    private String password;
}