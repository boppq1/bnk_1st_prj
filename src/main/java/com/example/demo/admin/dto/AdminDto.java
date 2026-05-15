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
    String admin_pw;
    String name;
    String admin_role;

    // Thymeleaf용 camelCase getter 수동 추가
    public Long getAdminId()     { return admin_id; }
    public String getLoginId()   { return login_id; }
    public String getAdminRole() { return admin_role; }
    public String getAdminPw()   { return admin_pw; }

}
