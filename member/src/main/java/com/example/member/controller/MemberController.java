package com.example.member.controller;

import com.example.member.entity.Member;
import com.example.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    ////localhost:8080/member/list
    //localhost:8080/member/list?keyword= 로이
    // ─── 목록 조회 + 검색 ──────────────────────────────────────
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword, Model model)
    {
        if (keyword != null && !keyword.isBlank()) {
//            model.addAttribute("members", memberService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("members", memberService.findAll());
        }
        return "member/list";
    }

    //localhost:member/new : get
    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("member", new Member());

        return "member/form";
    }

    //localhost:member/new : post
    @PostMapping("/new")
    public String newMember(@ModelAttribute Member member,
                          RedirectAttributes rttr){

        memberService.save(member);
        rttr.addFlashAttribute("message", "회원이 등록되었습니다.");

        return "redirect:/member/list";
    }

    //localhost:8080/member/edit/{5}
    @PostMapping("/edit/{id}")
    public String editMember(@PathVariable("id") String id,
                           Model model){
//        System.out.println("수정 처리");
        log.info("수정 처리");
        log.info("ID :  {}", id);
        model.addAttribute("id", id);

        return "redirect:member/list";
    }


    @PostMapping("/delete")
    public void deleteMember(){
//        System.out.println("삭제 처리");
        log.info("삭제 처리");
    }
}












