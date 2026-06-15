package com.example.jpasecurity.controller;

import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           Model model){
        JpaMember target = memberService.findById(id);

        log.info("taget username : {}", target.getUsername());
        log.info("user username : {}", user.getUsername());

        //검증 코드
        // ★ 본인 확인: DB에 저장된 대상 username와 로그인 사용자의 username를 비교
        // 타인이 URL을 직접 입력해도 여기서 차단됩니다
        if(!target.getUsername().equals(user.getUsername())){
            return "redirect:/member/list?error=forbidden";
        }

        model.addAttribute("member", target);

        return "member/editForm";
    }

}
