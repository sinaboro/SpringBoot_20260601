package com.example.jpamember.controller;

import com.example.jpamember.entity.JpaMember;
import com.example.jpamember.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //localhost:8080/member/list
    //localhost:8080/member/list?keyword=신
    @GetMapping("/list")
    public String list(@RequestParam(required = false)String keyword,
                       Model model) {
        if(keyword != null && !keyword.isEmpty()){
            List<JpaMember> search = memberService.search(keyword);
            model.addAttribute("members", search);
//            search.forEach(member-> log.info("{}",member));
        }else {
            List<JpaMember> memberList = memberService.findAll();
            model.addAttribute("members", memberList);
        }

        return "member/list";
    }

    // ─── 등록 폼 ──────────────────────────────────────────────
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("member", new JpaMember());
        return "member/form";
    }

    // ─── 등록 처리 ─────────────────────────────────────────────
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute JpaMember jpaMember,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "member/form";
        try {
            memberService.save(jpaMember);
            redirectAttributes.addFlashAttribute("message", "회원이 등록되었습니다.");
        } catch (IllegalStateException e) {
            result.rejectValue("email", "duplicate", e.getMessage());
            return "member/form";
        }
        return "redirect:/member/list";
    }
}
