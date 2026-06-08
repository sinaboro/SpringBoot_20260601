package com.example.member.mapper;

import com.example.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    void findAllTest(){

        List<Member> memberList = memberMapper.findAll();

        for( Member member :memberList)
            log.info("Member : {}", member);
    }

    @Test
    void findById(){
        long id = 3;
        Member member = memberMapper.findById(3L);
        log.info("Member : {}", member);
    }

    @Test
    void updateMember(){
        Member member = new Member(6L, "박길동", "park@test.com");

        int result = memberMapper.updateMember(member);

        log.info("result : {}", result);
    }

    @Test
    void insertMember(){
        Member member = new Member();
        member.setName("뽀양");
        member.setEmail("abc@abc.com");

        int result = memberMapper.insertMember(member);
        log.info("result : {}", result);
    }

    @Test
    void deleteMember(){

        log.info("resdult : {}", memberMapper.deleteMember(6L) );

    }

    @Test
    void findByNameContaining(){
        String keyword = "길동";

        /*
        List<Member> list = memberMapper.findByNameContaining(keyword);
        for(Member member : list)
            log.info("Member : {}", member );
         */

        memberMapper.findByNameContaining(keyword)
                .forEach(member -> log.info("Member : {}", member )  );

    }

    @Test
    void findByEmail(){
        String email = "park@test.com";

        String result = memberMapper.findByEmail(email) == null ? "사용 가능" : "이메일 중복";

        log.info("result : {}", result);
    }


}