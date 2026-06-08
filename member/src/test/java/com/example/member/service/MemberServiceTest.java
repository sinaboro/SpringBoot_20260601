package com.example.member.service;

import com.example.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void save(){

        Member member = new Member();
        member.setName("오길동");
        member.setEmail("user@user.com");

        memberService.save(member);

    }
}