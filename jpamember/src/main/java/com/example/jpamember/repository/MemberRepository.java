package com.example.jpamember.repository;

import com.example.jpamember.entity.JpaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<JpaMember, Long> {

    //이름으로 검색하는 쿼리를 작성하고 싶다..함수를 작성하고싶다.
}
