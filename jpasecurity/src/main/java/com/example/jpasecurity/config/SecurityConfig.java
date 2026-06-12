package com.example.jpasecurity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration: Spring이 이 클래스를 설정 클래스로 인식
// @EnableWebSecurity: Spring Security 활성화 선언
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    // ★ BCryptPasswordEncoder를 Spring Bean으로 등록
    // → MemberService에서 의존성 주입(@RequiredArgsConstructor)으로 사용
    // → 비밀번호 암호화: encode() / 검증: matches()
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ★ 보안 필터 체인 설정 — URL별 접근 권한, 로그인, 로그아웃 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        log.info("------------------securityFilterChain--------------------------");

        http
                // ── URL별 접근 권한 설정 ──
                .authorizeHttpRequests(auth -> auth
                        // /auth/** (회원가입·로그인)과 정적 리소스는 로그인 없이 접근 가능
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        // 그 외 모든 URL은 로그인 필수
                        // → 미로그인 상태로 접근하면 자동으로 loginPage로 이동
                        .anyRequest().authenticated()
                )

                // ── 로그인 설정 ──
                .formLogin(form -> form
                        // 커스텀 로그인 페이지 URL (기본값: /login)
                        .loginPage("/auth/login")
                        // POST 처리 URL — form의 action과 반드시 일치해야 합니다
                        .loginProcessingUrl("/auth/login")
                        // 로그인 성공 후 이동할 URL (true: 항상 이 URL로 이동)
                        .defaultSuccessUrl("/member/list", true)
                        // 로그인 실패 시 이동할 URL
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )

                 // ── 로그아웃 설정 ──
                .logout(logout -> logout
                    // 로그아웃 처리 URL — form method=POST로 요청해야 합니다
                    .logoutUrl("/logout")
                    // 로그아웃 성공 후 이동할 URL
                    .logoutSuccessUrl("/auth/login?logout=true")
                    .permitAll()
                );
        return http.build();
    }
}
