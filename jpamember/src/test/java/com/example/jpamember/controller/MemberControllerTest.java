package com.example.jpamember.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// 정적 import
// MockMvc의 요청 생성(get)
// 결과 검증(status, view, model 등)을 편하게 사용하기 위함
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 회원 컨트롤러 테스트
 *
 * @SpringBootTest
 *  - 실제 스프링 컨테이너를 띄워 테스트
 *
 * @AutoConfigureMockMvc
 *  - MockMvc 객체를 자동 생성
 *  - 서버를 실제 실행하지 않고 Controller 테스트 가능
 *
 * @Slf4j
 *  - 로그 객체(log) 자동 생성
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
class MemberControllerTest {

    /**
     * 가짜 브라우저 역할
     *
     * GET 요청
     * POST 요청
     * 응답 결과 검증
     *
     * 등을 수행할 수 있다.
     */
    @Autowired
    private MockMvc mockMvc;


    /**
     * 회원 목록 조회 테스트
     *
     * URL :
     * http://localhost:8080/member/list
     */
    @Test
    @DisplayName("회원 목록 전체 조회 테스트")
    public void list() throws Exception {

        // GET /member/list 요청 수행
        mockMvc.perform(get("/member/list"))

                // HTTP 상태 코드가 200인지 확인
                .andExpect(status().isOk())

                // 반환 View 이름이 member/list 인지 확인
                // return "member/list";
                .andExpect(view().name("member/list"))

                // Model 객체에 members 속성이 존재하는지 확인
                // model.addAttribute("members", memberList);
                .andExpect(model().attributeExists("members"));
    }


    /**
     * 회원 검색 테스트
     *
     * URL :
     * /member/list?keyword=신
     *
     * 검색어 : 신
     */
    @Test
    @DisplayName("회원 검색 테스트")
    public void search() throws Exception {

        mockMvc.perform(

                        // GET 요청
                        get("/member/list")

                                // 쿼리스트링 추가
                                // ?keyword=신
                                .param("keyword", "신")
                )

                // HTTP 200 확인
                .andExpect(status().isOk())

                // member/list 화면 반환 확인
                .andExpect(view().name("member/list"))

                // Model에 members 존재 여부 확인
                .andExpect(model().attributeExists("members"));
    }

}