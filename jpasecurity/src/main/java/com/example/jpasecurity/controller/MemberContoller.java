package com.example.jpasecurity.controller;

import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 수정 처리 — POST /member/edit/{id}
    // @RequestParam: 폼에서 name 속성으로 전송된 값을 받음
    // phone은 선택 입력이므로 required = false
    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @AuthenticationPrincipal User user ,
            RedirectAttributes redirectAttributes) {

        JpaMember target = memberService.findById(id);
        // ★ POST 위변조 방지 — GET과 POST 양쪽에서 모두 본인 확인
        // 악의적인 사용자가 POST 요청을 직접 보내는 경우도 차단
        if (!target.getUsername().equals(user.getUsername())) {
            return "redirect:/member/list?error=forbidden";
        }

        memberService.update(id, name, email, phone);
        // RedirectAttributes: 리다이렉트 후에도 1회 메시지를 전달 (Flash Attribute)
        redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");

        return "redirect:/member/list";
    }

    // 삭제 처리 — GET /member/delete/{id}
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {
        JpaMember target = memberService.findById(id);
        // ★ 본인 확인 — 타인의 게시글 삭제 시도 차단
        if (!target.getUsername().equals(user.getUsername())) {
            return "redirect:/member/list?error=forbidden";
        }

        memberService.delete(id);
        redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");

        return "redirect:/member/list";
    }


}
