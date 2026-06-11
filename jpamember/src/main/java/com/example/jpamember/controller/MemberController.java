package com.example.jpamember.controller;

import com.example.jpamember.entity.JpaMember;
import com.example.jpamember.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //localhost:8080/member/list
    //localhost:8080/member/list?keyword=신
    @GetMapping("/member/list")
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
}
