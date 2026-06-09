package com.example.jpamember.repository;

import com.example.jpamember.entity.JpaMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save- 저장후 id로 단건 조회가 가능해야 한다.")
    void saveAndFindById(){

        //given - 실행 데이타 제공
        /*
        JpaMember jpaMember = new JpaMember();
        jpaMember.setName("테스트유저");
        jpaMember.setEmail("test@test.com");
         */

        JpaMember jpaMember = JpaMember.builder()
                .name("테스트유저")
                .email("test@test.com")
                .build();

        //when - 실행(DB 실행)
        JpaMember saved = memberRepository.save(jpaMember);

        Optional<JpaMember>  found = memberRepository.findById(saved.getId());
        log.info("found : {}", found);

        //then - 검증
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("테스트유저");
        System.out.println("저장된 회원: " + saved);
    }

    // ─── 테스트 2: 수정 ─────────────────────────────────────
    @Test
    @DisplayName("save - 수정 후 변경값이 반영되어야 한다")
    void update_test() {
        // given
        JpaMember jpaMember = new JpaMember();
        jpaMember.setName("수정전이름");
        jpaMember.setEmail("before@test.com");
        JpaMember saved = memberRepository.save(jpaMember);

        // when - 조회 후 값 변경 → save() 재호출
        saved.setName("수정후이름"); saved.setEmail("after@test.com");
        memberRepository.save(saved);

        // then
        //JpaMember updated = memberRepository.findById(saved.getId()).get();
        //assertThat(updated.getName()).isEqualTo("수정후이름");
    }

    @Test
    @Transactional
    @DisplayName("더티체킹 테스트")
    @Rollback(value = false)
    void dirty_check_test() {

        // given
        JpaMember member = new JpaMember();
        member.setName("수정전");
        member.setEmail("before@test.com");

        JpaMember saved = memberRepository.save(member);

        // when
        saved.setName("수정후");
        saved.setEmail("after@test.com");

        // save() 없음

        // then
    }

    // ─── 테스트 3: 삭제 ─────────────────────────────────────
    @Test
    @DisplayName("deleteById - 삭제 후 findById 결과가 empty이어야 한다")
    void delete_test() {
        JpaMember jpaMember = new JpaMember();
        jpaMember.setName("삭제대상");
        jpaMember.setEmail("del@test.com");
        JpaMember saved = memberRepository.save(jpaMember);

        // when
        memberRepository.deleteById(2L);

        // then - JPA는 null 대신 Optional.empty() 반환
        Optional<JpaMember> deleted = memberRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    // ─── 테스트 4: 이름 검색 ──────────────────────────────────
    @Test
    @DisplayName("findByNameContaining - 키워드가 포함된 회원만 반환")
    void findByNameContaining_test() {
        JpaMember m1 = new JpaMember();
        m1.setName("홍길동");
        m1.setEmail("h1@a.com");

        JpaMember m2 = new JpaMember();
        m2.setName("홍문표");
        m2.setEmail("h2@t.com");

        JpaMember m3 = new JpaMember();
        m3.setName("김철수");
        m3.setEmail("k@t.com");

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        // when
        List<JpaMember> result = memberRepository.findByEmailContaining("t.com");

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(m -> m.getEmail().contains("t.com"));

        result.forEach(member-> log.info("member : {}", member));
    }

}