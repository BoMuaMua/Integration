package com.example.signin.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUserDetail {
    private Long id;
    private String userName;
    private String departmentName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String signinTime;
    private String status;
}
