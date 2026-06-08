package com.example.member.service;

import com.example.member.entity.Member;
import com.example.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MemberService {

    // MemberMapper 객체를 스프링이 자동으로 주입
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 전체 회원 조회
     */
    public List<Member> findAll() {

        // 서비스 호출 확인용 로그
        log.info("MemberService 호출...........");

        // DB에서 전체 회원 목록 조회 후 반환
        return memberMapper.findAll();
    }

    /**
     * 이름 검색
     * 예) 홍길동 검색
     */
    public List<Member> search(String keyword) {

        // 이름에 keyword가 포함된 회원 목록 조회
        return memberMapper.findByNameContaining(keyword);
    }

    /**
     * 회원 1명 조회
     */
    public Member findById(Long id) {

        // id로 회원 조회
        Member member = memberMapper.findById(id);

        // 회원이 없으면 예외 발생
        if (member == null) {
            throw new IllegalArgumentException(
                    "해당 회원이 없습니다. id=" + id);
        }

        // 조회된 회원 반환
        return member;
    }

    /**
     * 회원 등록
     */
    @Transactional
    public void save(Member member) {

        /*
         회원 가입 시 수행되는 작업 예시

         1. 회원 정보 저장
         2. 신규 가입 포인트 지급
         3. 가입 로그 저장

         위 작업 중 하나라도 실패하면
         모두 취소하기 위해 @Transactional 사용
         */

        // 같은 이메일이 이미 존재하는지 확인
        Member existing =
                memberMapper.findByEmail(member.getEmail());

        // 이미 사용 중인 이메일이면 가입 불가
        if (existing != null) {
            throw new IllegalArgumentException(
                    "이미 사용 중인 이메일 입니다.");
        }

        // 회원 정보 저장
        memberMapper.insertMember(member);
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long id, Member updateData) {

        // 수정할 회원 조회
        Member member = memberMapper.findById(id);

        // 조회한 회원 객체에 새로운 값 설정
        member.setName(updateData.getName());
        member.setEmail(updateData.getEmail());

        // DB 수정
        // (실제로는 updateMember()가 더 적절)
//        memberMapper.insertMember(member);
        memberMapper.updateMember(member);
    }

    /**
     * 회원 삭제
     */
    public void delete(Long id) {

        // id에 해당하는 회원 삭제
        memberMapper.deleteMember(id);
    }
}