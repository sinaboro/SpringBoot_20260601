package com.example.member.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor //생성자
@NoArgsConstructor  //디폴트 생성자
@ToString
public class Member {
    private Long id;

    private String name;

    private String email;

}
