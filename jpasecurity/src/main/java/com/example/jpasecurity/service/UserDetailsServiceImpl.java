package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*
        return memberRepository.findByUsername(username)
                // JpaMember를 UserAccount로 변환 (메서드 참조 방식)
                .map(UserAccount::new)
                // 해당 username이 없으면 예외 발생 → 로그인 실패 처리됨
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "사용자를 찾을 수 없습니다: " + username));
         */

        JpaMember member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.: " + username));

        /*
        UserAccount userAccount = new UserAccount(member);

        return userAccount;
         */

        return User.builder()
                .username(member.getUsername())
                .password((member.getPassword()))
                //.roles("USER")
                .authorities(member.getRole())
                .build();

    }
}
