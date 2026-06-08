package com.example.member.controller;

import com.example.member.entity.Member;
import com.example.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String newMember(@Valid @ModelAttribute Member member,
                          BindingResult result,
                          RedirectAttributes rttr){

        if(result.hasErrors()){
            return "member/form";
        }

        /*
              memberService.save(member);
              rttr.addFlashAttribute("message", "회원이 등록되었습니다.");
              return "redirect:/member/list";
         */

        try {
            memberService.save(member);
            rttr.addFlashAttribute("message", "회원이 등록되었습니다.");
        }catch(Exception e){
            result.rejectValue("email", "duplicate", e.getMessage());
            return "member/form";
        }
        return "redirect:/member/list";
    }

    //회원 수정 폼
    //localhost:8080/member/edit/3 - get
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("member", memberService.findById(id));
        return "member/editForm";
    }

    //localhost:8080/member/edit/{5}
    @PostMapping("/edit/{id}")
    public String editMember(@PathVariable("id") Long id,
                           @Valid @ModelAttribute Member member,
                           BindingResult result,
                           RedirectAttributes rttr){

        if(result.hasErrors()) return  "member/editForm";

        memberService.update(id, member);
        rttr.addFlashAttribute("message", "회원 정보가 수정되었습니다.");

        return "redirect:/member/list";
    }

    //localhost:8080/member/delete/1
    //삭제 처리
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id,
                               RedirectAttributes rttr){

        memberService.delete(id);
        rttr.addFlashAttribute("message", "회원이 삭제되었습니다.");
        return "redirect:/member/list";
    }
}












