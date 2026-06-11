package com.example.jpamember.service;

import com.example.jpamember.entity.JpaMember;
import com.example.jpamember.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMember(){
        //memberRepository.save(new JpaMember(null, "홍길동", "hong@abc.com"));
        memberRepository.save(new JpaMember(null, "이순신", "lee@abc.com"));
        memberRepository.save(new JpaMember(null, "강감찬", "kang@test.com"));
        memberRepository.save(new JpaMember(null, "유관순", "yu@test.com"));
        memberRepository.save(new JpaMember(null, "김유신", "kim@test.com"));
        memberRepository.save(new JpaMember(null, "세종대왕", "sejong@test.com"));
        memberRepository.save(new JpaMember(null, "신사임당", "shin@test.com"));
        memberRepository.save(new JpaMember(null, "정약용", "jung@test.com"));
        memberRepository.save(new JpaMember(null, "안중근", "ahn@test.com"));
        memberRepository.save(new JpaMember(null, "윤봉길", "yoon@test.com"));

        System.out.println("저장 완료");
    }


    /** 전체 회원 조회 (id 내림차순) */
    @Test
    public void findAll() {
        List<JpaMember> memberList = memberService.findAll();

        memberList
                .forEach(member -> log.info("member : {}", member));
    }

    /** 이름 검색 */
    @Test
    public void search(){
        memberService.search("신")
                .forEach(member-> log.info("member : {}", member));
    }

}