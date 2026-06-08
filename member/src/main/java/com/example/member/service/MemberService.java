package com.example.member.service;

import com.example.member.entity.Member;
import com.example.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    /** 전체 회원 조회 */
    public List<Member>  findAll(){
        // code 입력
      log.info("MemberService 호출...........");
      return memberMapper.findAll();
    }

    //이름 검색
    public List<Member> search(String keyword){
        return memberMapper.findByNameContaining(keyword);
    }

    // 단건 조회
    public Member findById(Long id){
        Member member = memberMapper.findById(id);

        if(member == null){
            throw new IllegalArgumentException("해당 회원이 없습니다. id="+ id);
        }

        return member;
    }

    // 회원 등록( 이메일 중복 체크 포함 )
    @Transactional
    public void save(Member member){

        /*
         회원 가입시
         member 저장 insert()
         포인트 저장 WelcomePoint()
         로그 저장   insertLog()
         */

        Member existing = memberMapper.findByEmail(member.getEmail());

        if(existing != null){
            throw  new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
        }

        memberMapper.insertMember(member);
    }

    //회원 수정
    @Transactional
    public void update(Long id, Member updateData){

        //수정 대상 회원 조회
        Member member = memberMapper.findById(id);

        member.setName(updateData.getName());
        member.setEmail(updateData.getEmail());

        //수정
        memberMapper.insertMember(member);

    }

    //회원 삭제
    public void delete(Long id){
        memberMapper.deleteMember(id);
    }
}
