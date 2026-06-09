package com.example.jpamember.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name="jpa_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Data
@Builder
public class JpaMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //AUTO_INCREMENT
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;
}
