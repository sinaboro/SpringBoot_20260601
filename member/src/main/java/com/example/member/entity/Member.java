package com.example.member.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor //생성자
@NoArgsConstructor  //디폴트 생성자
@ToString
public class Member {
    private Long id;

    @NotBlank(message="이름을 입력하세요")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;

}
