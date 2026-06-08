package com.example.member.mapper;

import com.example.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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
    @DisplayName("update - 이름/이메일 수정 후 변경값이 반영되어야 한다")
    @Transactional
    void updateMember(){
        // given - 회원 등록
        Member member = new Member();
        member.setName("수정전이름");
        member.setEmail("before3@test.com");
        memberMapper.insertMember(member);

        // when - 이름·이메일 변경 후 update
        member.setName("수정후이름");
        member.setEmail("after3@test.com");
        memberMapper.updateMember(member);

        // then
        Member updated = memberMapper.findById(member.getId());
        assertThat(updated.getName()).isEqualTo("수정후이름");
        assertThat(updated.getEmail()).isEqualTo("after3@test.com");
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