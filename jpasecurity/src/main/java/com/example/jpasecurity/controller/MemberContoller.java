package com.example.jpasecurity.controller;

import com.example.jpasecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberContoller {

    private final MemberService memberService;

    // 회원 목록 조회 — GET /member/list
    @GetMapping("/list")
    public String list(
            // URL 파라미터: /member/list?keyword=홍 → keyword = "홍"
            // required = false: 파라미터 없어도 오류 발생 안 함
            @RequestParam(required = false) String keyword,
            Model model) {
        model.addAttribute("members", memberService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "member/list";
    }

}
