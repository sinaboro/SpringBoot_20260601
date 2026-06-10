package com.example.jpastudy.repository;

import com.example.jpastudy.entity.Member;
import com.example.jpastudy.entity.Order;
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

    @Autowired
    private OrderRepository orderRepository;

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

    @Test
    @Transactional
    void relationTest() {
        // 회원 저장
        Member member = new Member("홍길동", "hong@test.com", 30);
        memberRepository.save(member);

        // 주문 저장 (FK 자동 설정)
        Order o1 = new Order("노트북", 1500000, member);
        Order o2 = new Order("마우스", 35000, member);
        orderRepository.save(o1);
        orderRepository.save(o2);

        // 주문에서 회원 조회 (N+1 문제 없음 - LAZY)
        Order found = orderRepository.findById(o1.getId())
                .orElseThrow();

        System.out.println("주문자: " + found.getMember().getUsername());
        System.out.println("상품: " + found.getProduct());
    }

}