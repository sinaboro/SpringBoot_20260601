package com.example.jpastudy.repository;

import com.example.jpastudy.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    void queryTest() {
        // 데이터 세팅
        memberRepository.save(new Member("홍길동", "hong@test.com", 25));
        memberRepository.save(new Member("김철수", "kim@test.com", 32));
        memberRepository.save(new Member("이영희", "lee@test.com", 28));
        memberRepository.save(new Member("박민준", "park@test.com", 19));

        // 이름에 '길' 포함
        List<Member> result1 = memberRepository.findByUsernameContaining("길");
        System.out.println("'길' 포함: " + result1.size() + "명");

        // 나이 범위
        List<Member> result2 = memberRepository.findByAgeRange(20, 30);
        System.out.println("20~30세: " + result2.size() + "명");

        // 평균 나이
        Double avg = memberRepository.findAverageAge();
        System.out.println("평균 나이: " + avg);
    }

}