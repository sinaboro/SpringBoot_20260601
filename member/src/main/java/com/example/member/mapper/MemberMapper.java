package com.example.member.mapper;


import com.example.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    //전제 회원 조회
    public List<Member> findAll();

    // PK로 단건 조회
    public Member findById(@Param("id") Long id);

    // 이름 검색
    public List<Member> findByNameContaining(@Param("keyword") String keyword);

    // 이메일로 조회(중복 체크용)
    Member findByEmail(String email);

    // 회원 정보 수정
    public int updateMember(Member member);

    // 회원 저장
    public int insertMember(Member member);

    // 회원 삭제
    public int deleteMember(@Param("id") Long id);
}
