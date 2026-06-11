package com.example.jpamember.service;

import com.example.jpamember.entity.JpaMember;
import com.example.jpamember.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** 단건 조회 - Optional 처리 */
    public JpaMember findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));
    }
    /** 회원 등록 (이메일 중복 체크 포함) */
    @Transactional
    public void save(JpaMember jpaMember) {
        memberRepository.findByEmail(jpaMember.getEmail()).ifPresent(m -> {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        });
        memberRepository.save(jpaMember); // id 없으면 INSERT
    }
    /** 회원 수정 */
    @Transactional
    public void update(Long id, JpaMember updateData) {
        JpaMember jpaMember = findById(id);
        jpaMember.setName(updateData.getName());
        jpaMember.setEmail(updateData.getEmail());
        memberRepository.save(jpaMember); // id 있으면 UPDATE
    }
    /** 회원 삭제 */
    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }


}
