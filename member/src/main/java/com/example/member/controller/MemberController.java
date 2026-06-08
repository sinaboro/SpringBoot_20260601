package com.example.member.controller;

import com.example.member.entity.Member;
import com.example.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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


    //localhost:member/new
    @PostMapping("/new")
    public void newMember(@RequestBody Member member){
        //System.out.println("등록 처리!!");
        log.info("등록 처리!!");
        log.info("Member : {}", member);
        log.info("ID : {}", member.getId());
        log.info("Name : {}", member.getName());
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












