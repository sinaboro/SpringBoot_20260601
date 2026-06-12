package com.example.jpasecurity.controller;

import com.example.jpasecurity.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// 회원가입과 로그인 화면을 담당하는 Controller
// /auth 로 시작하는 URL은 SecurityConfig에서 permitAll() → 로그인 없이 접근 가능
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // 로그인 화면 — GET /auth/login
    // 로그인 처리(POST)는 Spring Security가 자동으로 처리
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login"; // templates/auth/login.html 반환
    }

    // 회원가입 화면 — GET /auth/register
    // RegisterDto 빈 객체를 모델에 담아 폼과 연결
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    // 회원가입 처리 — POST /auth/register
    // @Valid: RegisterDto의 검증 어노테이션 실행
    // BindingResult: 검증 결과를 담는 객체 (@Valid 바로 뒤에 선언해야 합니다)
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult result,
            Model model) {
        // 검증 오류가 있으면 회원가입 화면으로 돌아감 (오류 메시지 표시)
        if (result.hasErrors()) return "auth/register";
        try {
            memberService.register(dto); // 비밀번호 암호화 후 DB 저장
            // 회원가입 성공 → 로그인 화면으로 이동 (registered=true 파라미터 전달)
            return "redirect:/auth/login?registered=true";
        } catch (IllegalArgumentException e) {
            // 아이디·이메일 중복 시 오류 메시지를 화면에 전달
            model.addAttribute("errorMsg", e.getMessage());
            return "auth/register";
        }
    }


}
