package com.example.jpamember.service;

import com.example.jpamember.entity.JpaMember;
import com.example.jpamember.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /** 전체 회원 조회 (id 내림차순) */
    public List<JpaMember> findAll() {
        return memberRepository.findAllByOrderByIdDesc();
    }

    /** 이름 검색 */
    public List<JpaMember> search(String keyword) {
        return memberRepository.findByNameContaining(keyword);
    }

}
