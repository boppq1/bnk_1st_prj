package com.example.demo.admin.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AdminDto {

    Long admin_id;
    String login_id;
    String password;
    String department;
    Long admin_pw;
    String name;
    String admin_role;

}
